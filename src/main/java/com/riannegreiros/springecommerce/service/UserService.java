package com.riannegreiros.springecommerce.service;

import com.riannegreiros.springecommerce.entity.User;
import com.riannegreiros.springecommerce.utils.GetAllUsersResponse;

import java.util.UUID;

public interface UserService {
    GetAllUsersResponse getAllUsers(Integer page, Integer size, String sortBy, String sortDir);
    User save(User user);
    User updateUser(User user, UUID id);

    void deleteUser(UUID id);
}
