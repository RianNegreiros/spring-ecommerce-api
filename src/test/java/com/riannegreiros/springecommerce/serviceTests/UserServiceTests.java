package com.riannegreiros.springecommerce.serviceTests;

import com.riannegreiros.springecommerce.entity.User;
import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.repository.UserRepository;
import com.riannegreiros.springecommerce.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    private UserServiceImpl userService;
    private User user;

    @BeforeEach
    void setUp() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(passwordEncoder, userRepository);
        user = new User("any_mail@mail.com", "any_password", "any_name", "any_name");
    }

    @Test
    public void testSaveUser() {
        userService.save(user);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(user);
    }
    @Test
    public void testThrowIfEmailIsTaken() {
        given(userRepository.findUserByEmail(anyString()))
                .willReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.save(user))
                .isInstanceOf(Error.class)
                .hasMessageContaining("User already exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    public void testUpdateUser() {
        User updateUser = new User("update_mail@mail.com", "update_password", "update_name", "update_name");

        given(userRepository.findById(any()))
                .willReturn(Optional.ofNullable(user));

        userService.updateUser(updateUser, user.getId());

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser.getEmail()).isEqualTo(updateUser.getEmail());
        assertThat(capturedUser.getPassword()).isEqualTo(updateUser.getPassword());
        assertThat(capturedUser.getFirstName()).isEqualTo(updateUser.getFirstName());
        assertThat(capturedUser.getLastName()).isEqualTo(updateUser.getLastName());
    }

    @Test
    public void testThrowIfUserNotFound() {
        given(userRepository.findById(any()))
                .willThrow(new ResourceNotFoundException("User", "id", null));

        assertThatThrownBy(() -> userService.updateUser(user, UUID.randomUUID()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with %s : '%s'", "User", "id", null));

        verify(userRepository, never()).save(any());
    }
}
