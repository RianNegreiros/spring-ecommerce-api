package com.riannegreiros.springecommerce.modules.user.serviceTests;

import com.riannegreiros.springecommerce.AWS.StorageService;
import com.riannegreiros.springecommerce.modules.user.entity.User;
import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.user.repository.UserRepository;
import com.riannegreiros.springecommerce.modules.user.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

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
    private StorageService storageService;
    private User user;

    @BeforeEach
    void setUp() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(passwordEncoder, userRepository, storageService);
        user = new User("any_mail@mail.com", "any_password", "any_name", "any_name");
    }

    @Test
    public void testSave() {
        userService.save(user);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(user);
    }
    @Test
    public void testThrowIfEmailIsTaken() {
        given(userRepository.findUserByEmail(anyString()))
                .willThrow(new Error("User already exists with this email: " + user.getEmail()));

        assertThatThrownBy(() -> userService.save(user))
                .isInstanceOf(Error.class)
                .hasMessageContaining("User already exists with this email: " + user.getEmail());

        verify(userRepository, never()).save(any());
    }

    @Test
    public void testUpdate() {
        User updateUser = new User("update_mail@mail.com", "update_password", "update_name", "update_name");

        given(userRepository.findById(any()))
                .willReturn(Optional.ofNullable(user));

        userService.update(updateUser, user.getId());

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser.getEmail()).isEqualTo(updateUser.getEmail());
        assertThat(capturedUser.getPassword()).isEqualTo(updateUser.getPassword());
        assertThat(capturedUser.getFirstName()).isEqualTo(updateUser.getFirstName());
        assertThat(capturedUser.getLastName()).isEqualTo(updateUser.getLastName());
    }

    @Test
    public void testThrowIfNotFound() {
        given(userRepository.findById(any()))
                .willThrow(new ResourceNotFoundException("User", "id", null));

        assertThatThrownBy(() -> userService.update(user, UUID.randomUUID()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with %s : '%s'", "User", "id", null));

        verify(userRepository, never()).save(any());
    }

    @Test
    public void testFindAll() {
        User user1 = new User("any_mail@mail.com", "any_password", "any_name", "any_name");
        User user2 = new User("any_mail@mail.com", "any_password", "any_name", "any_name");
        User user3 = new User("any_mail@mail.com", "any_password", "any_name", "any_name");
        List<User> userList = Arrays.asList(user, user1, user2, user3);
        Page<User> userPage = new PageImpl<>(userList);
        given(userRepository.findAll(any(Pageable.class)))
                .willReturn(userPage);

        userService.findAll(0, 10, "id", "asc");

        ArgumentCaptor<Pageable> valueArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(userRepository).findAll(valueArgumentCaptor.capture());

        Pageable capturedValue = valueArgumentCaptor.getValue();

        assertThat(capturedValue).isInstanceOf(Pageable.class);
    }
}
