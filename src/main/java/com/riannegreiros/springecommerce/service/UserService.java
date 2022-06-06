package com.riannegreiros.springecommerce.service;

import com.riannegreiros.springecommerce.entity.User;
import com.riannegreiros.springecommerce.utils.GetAllUsersResponse;

import java.util.UUID;

public interface UserService {
    User save(User user);
    User updateUser(User user, UUID id);
    GetAllUsersResponse getAllUsers(Integer page, Integer size, String sortBy, String sortDir);
}
