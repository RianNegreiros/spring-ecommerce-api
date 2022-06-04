package com.riannegreiros.springecommerce.securityTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

    @Test
    public void testEncodePassword() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "any_password";
        String encodedPassword = passwordEncoder.encode(password);

        Assertions.assertTrue(passwordEncoder.matches(password, encodedPassword));
    }
}
