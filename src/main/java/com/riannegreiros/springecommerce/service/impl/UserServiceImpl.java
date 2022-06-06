package com.riannegreiros.springecommerce.service.impl;

import com.riannegreiros.springecommerce.entity.User;
import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.repository.UserRepository;
import com.riannegreiros.springecommerce.service.UserService;
import com.riannegreiros.springecommerce.utils.GetAllUsersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        Optional<User> findUser = userRepository.findUserByEmail(user.getEmail());
        if (findUser.isPresent()) {
            throw new Error("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User update(User user, UUID id) {
        User findUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));

        findUser.setEmail(user.getEmail());
        findUser.setFirstName(user.getFirstName());
        findUser.setLastName(user.getLastName());
        findUser.setPassword(user.getPassword());
        findUser.setPhoto(user.getPhoto());
        findUser.setRoles(user.getRoles());

        return userRepository.save(findUser);
    }

    @Override
    public GetAllUsersResponse findAll(Integer page, Integer size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userRepository.findAll(pageable);
        List<User> userList = users.getContent();

        return new GetAllUsersResponse(userList,
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isLast());
    }

    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
        userRepository.delete(user);
    }
}
