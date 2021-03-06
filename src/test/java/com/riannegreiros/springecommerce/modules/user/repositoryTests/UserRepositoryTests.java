package com.riannegreiros.springecommerce.modules.user.repositoryTests;

import com.riannegreiros.springecommerce.modules.user.entity.Role;
import com.riannegreiros.springecommerce.modules.user.entity.User;
import com.riannegreiros.springecommerce.modules.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;
    private User user;
    private User savedUser;

    @BeforeEach
    public void setUp() {
        user = new User("any_mail@mail.com", "any_password", "any_name", "any_name");
        savedUser = userRepository.save(user);
    }

    @Test
    public void testSave() {
        assertThat(user.getId()).isEqualTo(savedUser.getId());
    }

    @Test
    public void testAddRole() {
        savedUser.addRole(new Role("any_role", "any_role_description"));
        assertThat(savedUser.getRoles()).isNotEmpty();
    }

    @Test
    public void testRemoveRole() {
        savedUser.addRole(new Role("any_role", "any_role_description"));
        savedUser.removeRole("any_role");
        assertThat(user.getRoles()).isNullOrEmpty();
    }

    @Test
    public void testFindByEmail() {
        assertThat(userRepository.findUserByEmail("any_mail@mail.com")).isNotNull();
    }

    @Test
    public void testFindAllByKeyword() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        userRepository.findAllByKeyword("any_mail", pageable);

        assertThat(userRepository.findAllByKeyword("any_mail", pageable)).isNotEmpty();
        assertThat(userRepository.findAllByKeyword("any_mail", pageable)).first().isEqualTo(user);
    }
}
