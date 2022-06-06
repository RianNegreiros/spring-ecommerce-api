package com.riannegreiros.springecommerce.service;

import com.riannegreiros.springecommerce.entity.User;

import java.util.UUID;

public interface UserService {
    User save(User user);

    User updateUser(User user, UUID id);
}
