package com.riannegreiros.springecommerce.repositoryTests;

import com.riannegreiros.springecommerce.entity.Role;
import com.riannegreiros.springecommerce.entity.User;
import com.riannegreiros.springecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
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
        assertThat(user.getId()).isEqualTo(savedUser.getId());
    }

    @Test
    public void testAddRole() {
        savedUser.addRole(new Role("any_role", "any_role_description"));
        savedUser.addRole(new Role("any_role", "any_role_description"));
        assertThat(savedUser.getRoles()).isNotEmpty();
    }

    @Test
    public void testRemoveRole() {
        Role role = new Role("any_role", "any_role_description");
        Role role1 = new Role("any_role1", "any_role_description1");

        savedUser.addRole(role);
        savedUser.addRole(role1);
        assertThat(user.getRoles()).contains(role, role1);

        savedUser.getRoles().remove(role);
        assertThat(user.getRoles()).doesNotContain(role);

        savedUser.getRoles().remove(role1);
        assertThat(user.getRoles()).isNullOrEmpty();
    }

    @Test
    public void testFindUserByEmail() {
        assertThat(userRepository.findUserByEmail("any_mail@mail.com")).isNotNull();
    }
}
