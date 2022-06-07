package com.riannegreiros.springecommerce.modules.user.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface ImageService {
    void save(MultipartFile multipartFile, UUID id) throws IOException;
    String getPath(UUID id);
}
