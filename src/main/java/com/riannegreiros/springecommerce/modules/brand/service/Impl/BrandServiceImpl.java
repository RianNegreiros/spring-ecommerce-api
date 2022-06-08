package com.riannegreiros.springecommerce.modules.brand.service.Impl;

import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.brand.entity.Brand;
import com.riannegreiros.springecommerce.modules.brand.repository.BrandRepository;
import com.riannegreiros.springecommerce.modules.brand.service.BrandService;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Brand> brands = brandRepository.findAll(pageable);
        List<Brand> brandList = brands.getContent();

        return new FindAllResponse(brandList,
                brands.getNumber(),
                brands.getSize(),
                brands.getTotalElements(),
                brands.getTotalPages(),
                brands.isLast());
    }

    @Override
    public Brand save(Brand brand) {
        Brand brandExist = brandRepository.findByName(brand.getName());
        if (brandExist != null) {
            throw new Error("Category already exists with this name: " + brand.getName());
        }
        return brandRepository.save(brand);
    }

    @Override
    public Brand update(Brand brand) {
        Brand brandExist = brandRepository.findByName(brand.getName());
        if (brandExist == null) {
            throw new ResourceNotFoundException("Brand", "Name", brand.getName());
        }

        brandExist.setName(brand.getName());
        brandExist.setLogo(brand.getLogo());
        brandExist.setCategories(brand.getCategories());

        return brandRepository.save(brandExist);
    }
}
