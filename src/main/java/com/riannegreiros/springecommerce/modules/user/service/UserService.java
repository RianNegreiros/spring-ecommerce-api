package com.riannegreiros.springecommerce.modules.user.service;

import com.riannegreiros.springecommerce.modules.user.entity.User;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Writer;
import java.util.UUID;

public interface UserService {
    FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir);
    byte[] findImage(UUID id) throws IOException;
    User save(User user);
    User update(User user, UUID id);
    void delete(UUID id) throws IOException;

    void writeUsersToCSV(Writer writer) throws IOException;
    void saveImage(MultipartFile multipartFile, UUID id) throws IOException;
}
