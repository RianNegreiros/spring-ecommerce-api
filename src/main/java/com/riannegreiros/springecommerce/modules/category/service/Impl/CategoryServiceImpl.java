package com.riannegreiros.springecommerce.modules.category.service.Impl;

import com.riannegreiros.springecommerce.modules.category.entity.Category;
import com.riannegreiros.springecommerce.modules.category.repository.CategoryRepository;
import com.riannegreiros.springecommerce.modules.category.service.CategoryService;
import com.riannegreiros.springecommerce.modules.user.entity.User;
import com.riannegreiros.springecommerce.modules.user.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.utils.FileUploadUtil;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Category> categories = categoryRepository.findAll(pageable);
        List<Category> categoryList = categories.getContent();

        return new FindAllResponse(categoryList,
                categories.getNumber(),
                categories.getSize(),
                categories.getTotalElements(),
                categories.getTotalPages(),
                categories.isLast());
    }

    @Override
    public FindAllResponse findAllRootCategories() {
        List<Category> categories = categoryRepository.findAllRootCategories();
        Page<Category> categoryPage = new PageImpl<>(categories);
        List<Category> categoryList = categoryPage.getContent();

        return new FindAllResponse(categoryList,
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryPage.isLast());
    }

    @Override
    public Category save(Category category) {
        Category categoryExists = categoryRepository.findByName(category.getName());
        if (categoryExists != null) {
            throw new Error("Category already exists!");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category, Long id) {
        Category categoryExist = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("category", "id", id.toString()));

        categoryExist.setName(category.getName());
        categoryExist.setAlias(category.getAlias());
        categoryExist.setImage(category.getImage());
        if(categoryExist.getParent() != null) categoryExist.setParent(category.getParent());
        categoryExist.setChildren(category.getChildren());

        return categoryRepository.save(categoryExist);
    }

    @Override
    public void saveImage(MultipartFile multipartFile, Long id) throws IOException {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        category.setImage(fileName);
        String uploadDir = "categories-images/" + category.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    }
}
