package com.riannegreiros.springecommerce.modules.brand.service.Impl;

import com.riannegreiros.springecommerce.AWS.StorageService;
import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.brand.entity.Brand;
import com.riannegreiros.springecommerce.modules.brand.repository.BrandRepository;
import com.riannegreiros.springecommerce.modules.brand.service.BrandService;
import com.riannegreiros.springecommerce.modules.category.entity.Category;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    private final StorageService storageService;

    public BrandServiceImpl(BrandRepository brandRepository, StorageService storageService) {
        this.brandRepository = brandRepository;
        this.storageService = storageService;
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
    public FindAllResponse findAllByKeyword(String keyword, Integer page, Integer size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Brand> brandPage = brandRepository.findAllByKeyword(keyword, pageable);
        List<Brand> brandList = brandPage.toList();

        return new FindAllResponse(brandList,
                brandPage.getNumber(),
                brandPage.getSize(),
                brandPage.getTotalElements(),
                brandPage.getTotalPages(),
                brandPage.isLast());
    }

    @Override
    public Brand save(Brand brand) {
        Optional<Brand> brandExist = brandRepository.findByName(brand.getName());
        if (brandExist.isPresent()) {
            throw new Error("Brand already exists with this name: " + brand.getName());
        }
        return brandRepository.save(brand);
    }

    @Override
    public Brand update(Brand brand) {
        Brand brandExist = brandRepository.findByName(brand.getName()).orElseThrow(() -> new ResourceNotFoundException("Brand", "Name", brand.getName()));

        brandExist.setName(brand.getName());
        brandExist.setLogo(brand.getLogo());
        brandExist.setCategories(brand.getCategories());

        return brandRepository.save(brandExist);
    }

    @Override
    public void delete(Long id) {
        Brand brandExist = brandRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Brand", "Id", id.toString()));
        storageService.deleteFile(brandExist.getLogoImagePath());
        brandRepository.delete(brandExist);
    }

    @Override
    public byte[] findImage(Long id) throws IOException {
        Brand brandExist = brandRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("category", "id", id.toString()));
        return storageService.downloadFile(brandExist.getLogoImagePath());
    }

    @Override
    public void saveImage(MultipartFile multipartFile, Long id) throws IOException {
        Brand brandExist = brandRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("category", "id", id.toString()));
        String fileName = brandExist.getId().toString() + multipartFile.getOriginalFilename();
        storageService.uploadFile(fileName, multipartFile);
        brandExist.setLogo(fileName);
    }
}
