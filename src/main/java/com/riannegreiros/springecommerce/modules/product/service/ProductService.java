package com.riannegreiros.springecommerce.modules.product.service;

import com.riannegreiros.springecommerce.utils.FindAllResponse;

public interface ProductService {
    FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir);
}
