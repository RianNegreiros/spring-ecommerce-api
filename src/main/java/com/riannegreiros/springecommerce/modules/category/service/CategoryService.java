package com.riannegreiros.springecommerce.modules.category.service;

import com.riannegreiros.springecommerce.modules.category.entity.Category;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public interface CategoryService {
    Category findCategory(String alias);
    FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir);
    FindAllResponse findAllRootCategories();
    FindAllResponse findAllByKeyword(String keyword, Integer page, Integer size, String sortBy, String sortDir);
    FindAllResponse findAllEnabled();
    void writeCategoriesToCSV(Writer writer) throws IOException;
    Category save(Category category);
    Category update(Category category, Long id);
    void updateEnabledStatus(Long id, boolean status);
    void delete(Long id) throws IOException;
    void saveImage(MultipartFile multipartFile, Long id) throws IOException;
    byte[] findImage(Long id) throws IOException;
}
