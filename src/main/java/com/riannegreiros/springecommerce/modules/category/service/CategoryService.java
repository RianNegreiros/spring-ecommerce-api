package com.riannegreiros.springecommerce.modules.category.service;

import com.riannegreiros.springecommerce.modules.category.entity.Category;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CategoryService {
    FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir);
    FindAllResponse findAllRootCategories();
    Category save(Category category);
    Category update(Category category, Long id);
    void delete(Long id) throws IOException;
    void saveImage(MultipartFile multipartFile, Long id) throws IOException;
}
