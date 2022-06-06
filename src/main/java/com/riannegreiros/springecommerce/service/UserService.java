package com.riannegreiros.springecommerce.service;

import com.riannegreiros.springecommerce.entity.User;
import com.riannegreiros.springecommerce.utils.GetAllUsersResponse;

import java.io.IOException;
import java.io.Writer;
import java.util.UUID;

public interface UserService {
    GetAllUsersResponse findAll(Integer page, Integer size, String sortBy, String sortDir);
    User save(User user);
    User update(User user, UUID id);
    void delete(UUID id);

    User findByEmail(String email);
    void writeUsersToCSV(Writer writer) throws IOException;
}
