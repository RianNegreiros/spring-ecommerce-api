package com.riannegreiros.springecommerce.serviceTests;

import com.riannegreiros.springecommerce.entity.User;
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

    @BeforeEach
    void setUp() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(passwordEncoder, userRepository);
    }

    @Test
    public void testSaveUser() {
        User user = new User("any_mail@mail.com", "any_password", "any_name", "any_name");
        userService.save(user);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(user);
    }
    @Test
    public void testThrowIfEmailIsTaken() {
        User user = new User("any_mail@mail.com", "any_password", "any_name", "any_name");
        given(userRepository.findUserByEmail(anyString()))
                .willReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.save(user))
                .isInstanceOf(Error.class)
                .hasMessageContaining("User already exists");

        verify(userRepository, never()).save(any());
    }
}
