package com.riannegreiros.springecommerce.modules.category.controller;

import com.riannegreiros.springecommerce.modules.category.entity.Category;
import com.riannegreiros.springecommerce.modules.category.service.CategoryService;
import com.riannegreiros.springecommerce.utils.AppConstants;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "CRUD for category resource", tags = {"Category Controller"})
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation(value = "Get category by alias")
    @GetMapping(value = "/{alias}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> findCategory(@PathVariable(name = "alias") String alias) {
        Category category = categoryService.findCategory(alias);
        return new ResponseEntity<>(category, HttpStatus.FOUND);
    }

    @ApiOperation(value = "Get all categories with given keyword with pagination")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public FindAllResponse findAllByKeyword(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer size,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        if (keyword.isBlank()) return categoryService.findAll(page, size, sortBy, sortDir);
        return categoryService.findAllByKeyword(keyword, page, size, sortBy, sortDir);
    }

    @ApiOperation(value = "Get root categories")
    @GetMapping(value = "/root", produces = MediaType.APPLICATION_JSON_VALUE)
    public FindAllResponse findAllRoot() {
        return categoryService.findAllRootCategories();
    }

    @ApiOperation(value = "Get enabled categories")
    @GetMapping(value = "/enable", produces = MediaType.APPLICATION_JSON_VALUE)
    public FindAllResponse findAllEnabled() {
        return categoryService.findAllEnabled();
    }

    @ApiOperation(value = "Create a file .csv with all categories")
    @GetMapping("/csv")
    public void categoriesInCSV(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"categories.csv\"");
        categoryService.writeCategoriesToCSV(servletResponse.getWriter());
    }

    @ApiOperation(value = "Create a new category")
    @PostMapping()
    public ResponseEntity<Category> save(@RequestBody Category category, @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws IOException {
        Category savedCategory = categoryService.save(category);
        if (!multipartFile.isEmpty()) categoryService.saveImage(multipartFile, savedCategory.getId());
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update a category")
    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@RequestBody Category category, @PathVariable(name = "id") Long id) {
        Category updatedCategory = categoryService.update(category, id);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @ApiOperation(value = "Update category enable status")
    @PatchMapping("/{id}/enabled/{status}")
    public ResponseEntity<String> updateEnabledStatus(@PathVariable(name = "id") Long id, @PathVariable(name = "status") Boolean status) {
        categoryService.updateEnabledStatus(id, status);
        return new ResponseEntity<>("Category status has been deleted successfully updated to: " + status, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a category")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) throws IOException {
        categoryService.delete(id);
        return new ResponseEntity<>("Category has been deleted successfully", HttpStatus.OK);
    }

    @ApiOperation(value = "Upload category image")
    @PostMapping("/image/{id}")
    public ResponseEntity<String> saveImage(@RequestParam("image") MultipartFile multipartFile, @PathVariable(name = "id") Long id) throws IOException {
        categoryService.saveImage(multipartFile, id);
        return new ResponseEntity<>("Image has been saved successfully", HttpStatus.CREATED);
    }

    @ApiOperation(value = "Download category image")
    @GetMapping("/image/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(name = "id") Long id) throws IOException {
        byte[] data = categoryService.findImage(id);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment: filename=\"" + id + "\"")
                .body(resource);
    }
}
