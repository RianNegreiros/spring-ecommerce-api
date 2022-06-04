package com.riannegreiros.springecommerce.service;

import com.riannegreiros.springecommerce.entity.User;

public interface UserService {
    User save(User user);
    User GetUserByEmail(String email);
}
