package com.riannegreiros.springecommerce.repositoryTests;

import com.riannegreiros.springecommerce.entity.Role;
import com.riannegreiros.springecommerce.entity.User;
import com.riannegreiros.springecommerce.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private User user;
    private User savedUser;

    @BeforeEach
    public void createUser() {
        user = new User("any_mail@mail.com", "any_password", "any_name", "any_name");
        savedUser = userRepository.save(user);
    }

    @Test
    public void testCreateUser() {
        Assertions.assertEquals(user.getId(), savedUser.getId());
    }

    @Test
    public void testAddRole() {
        savedUser.addRole(new Role("any_role", "any_role_description"));
        savedUser.addRole(new Role("any_role", "any_role_description"));
        Assertions.assertFalse(user.getRoles().isEmpty());
    }

    @Test
    public void testRemoveRole() {
        Role role = new Role("any_role", "any_role_description");

        savedUser.addRole(role);
        Assertions.assertFalse(user.getRoles().isEmpty());

        savedUser.getRoles().remove(role);
        Assertions.assertTrue(user.getRoles().isEmpty());
    }
}
