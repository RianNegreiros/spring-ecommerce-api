package com.riannegreiros.springecommerce.modules.user.securityTests;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncoderTest {

    @Test
    public void testEncodePassword() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "any_password";
        String encodedPassword = passwordEncoder.encode(password);
        Boolean matches = passwordEncoder.matches(password, encodedPassword);

        assertThat(matches).isTrue();
    }
}
