package com.riannegreiros.springecommerce.modules.brand.service;

import com.riannegreiros.springecommerce.modules.brand.entity.Brand;
import com.riannegreiros.springecommerce.utils.FindAllResponse;

public interface BrandService {
    FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir);
    FindAllResponse findAllByKeyword(String keyword, Integer page, Integer size, String sortBy, String sortDir);
    Brand save(Brand brand);
    Brand update(Brand brand);
    void delete(Long id);
}
