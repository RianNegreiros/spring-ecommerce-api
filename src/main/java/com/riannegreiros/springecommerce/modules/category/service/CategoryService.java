package com.riannegreiros.springecommerce.modules.category.service;

import com.riannegreiros.springecommerce.modules.category.entity.Category;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CategoryService {
    FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir);
    Category save(Category category);
    void saveImage(MultipartFile multipartFile, Long id) throws IOException;
}
