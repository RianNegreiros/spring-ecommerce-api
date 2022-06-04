package com.riannegreiros.springecommerce.service;

import com.riannegreiros.springecommerce.entity.User;

public interface UserService {
    void save(User user);
    User GetUserByEmail(String email);
}
