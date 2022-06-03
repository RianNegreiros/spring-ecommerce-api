package com.riannegreiros.springecommerce.repositoryTests;

import com.riannegreiros.springecommerce.entity.Role;
import com.riannegreiros.springecommerce.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateRole() {
        Role role = new Role("any_name", "any_description");
        Role savedRole = roleRepository.save(role);
        Assertions.assertEquals(role.getId(), savedRole.getId());
    }
}
