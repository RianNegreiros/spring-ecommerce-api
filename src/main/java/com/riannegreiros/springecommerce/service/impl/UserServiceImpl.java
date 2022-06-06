package com.riannegreiros.springecommerce.service.impl;

import com.riannegreiros.springecommerce.entity.User;
import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.repository.UserRepository;
import com.riannegreiros.springecommerce.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User GetUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User updateUser(User user, UUID id) {
        User findUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));

        findUser.setEmail(user.getEmail());
        findUser.setFirstName(user.getFirstName());
        findUser.setLastName(user.getLastName());
        findUser.setPassword(user.getPassword());
        findUser.setPhoto(user.getPhoto());
        findUser.setRoles(user.getRoles());

        return userRepository.save(findUser);
    }
}
