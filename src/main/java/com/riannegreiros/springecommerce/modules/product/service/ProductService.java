package com.riannegreiros.springecommerce.modules.product.service;

import com.riannegreiros.springecommerce.modules.product.entity.Product;
import com.riannegreiros.springecommerce.utils.FindAllResponse;

public interface ProductService {
    FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir);
    Product save (Product product);
    void updateEnabledStatus(Long id, boolean status);
}
