package com.riannegreiros.springecommerce.modules.user.service;

import com.riannegreiros.springecommerce.modules.user.entity.User;
import com.riannegreiros.springecommerce.utils.FindAllResponse;

import java.io.IOException;
import java.io.Writer;
import java.util.UUID;

public interface UserService {
    FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir);
    User save(User user);
    User update(User user, UUID id);
    void delete(UUID id);

    User findByEmail(String email);
    void writeUsersToCSV(Writer writer) throws IOException;
}
