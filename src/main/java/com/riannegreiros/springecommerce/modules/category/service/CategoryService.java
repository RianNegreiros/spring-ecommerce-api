package com.riannegreiros.springecommerce.modules.category.service;

import com.riannegreiros.springecommerce.modules.category.entity.Category;
import com.riannegreiros.springecommerce.utils.FindAllResponse;

public interface CategoryService {
    FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir);
    Category save(Category category);
}
