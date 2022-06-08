package com.riannegreiros.springecommerce.modules.brand.service.Impl;

import com.riannegreiros.springecommerce.modules.brand.entity.Brand;
import com.riannegreiros.springecommerce.modules.brand.repository.BrandRepository;
import com.riannegreiros.springecommerce.modules.brand.service.BrandService;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public Brand save(Brand brand) {
        Brand brandExist = brandRepository.findByName(brand.getName());
        if (brandExist != null) {
            throw new Error("Category already exists with this name: " + brand.getName());
        }
        return brandRepository.save(brand);
    }
}
