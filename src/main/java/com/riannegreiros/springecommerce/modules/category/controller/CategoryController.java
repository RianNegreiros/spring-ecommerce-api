package com.riannegreiros.springecommerce.modules.category.controller;

import com.riannegreiros.springecommerce.modules.category.entity.Category;
import com.riannegreiros.springecommerce.modules.category.service.CategoryService;
import com.riannegreiros.springecommerce.utils.AppConstants;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public FindAllResponse listAll(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer size,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return categoryService.findAll(page, size, sortBy, sortDir);
    }

    @PostMapping()
    public ResponseEntity<Category> save(@RequestBody Category category, @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws IOException {
        Category savedCategory = categoryService.save(category);
        categoryService.saveImage(multipartFile, savedCategory.getId());
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<String> saveImage(@RequestParam("image") MultipartFile multipartFile, @PathVariable(name = "id") Long id) throws IOException {
        categoryService.saveImage(multipartFile, id);
        return new ResponseEntity<>("Image has been saved successfully", HttpStatus.CREATED);
    }
}
