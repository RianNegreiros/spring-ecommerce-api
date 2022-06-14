package com.riannegreiros.springecommerce.modules.category.controller;

import com.riannegreiros.springecommerce.modules.category.entity.Category;
import com.riannegreiros.springecommerce.modules.category.service.CategoryService;
import com.riannegreiros.springecommerce.utils.AppConstants;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{alias}")
    public ResponseEntity<Category> findCategory(@PathVariable(name = "alias") String alias) {
        Category category = categoryService.findCategory(alias);
        return new ResponseEntity<>(category, HttpStatus.FOUND);
    }

    @GetMapping
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

    @GetMapping("/root")
    public FindAllResponse findAllRoot() {
        return categoryService.findAllRootCategories();
    }

    @GetMapping("/enable")
    public FindAllResponse findAllEnabled() {
        return categoryService.findAllEnabled();
    }

    @GetMapping("/csv")
    public void categoriesInCSV(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"categories.csv\"");
        categoryService.writeCategoriesToCSV(servletResponse.getWriter());
    }

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

    @PostMapping()
    public ResponseEntity<Category> save(@RequestBody Category category, @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws IOException {
        Category savedCategory = categoryService.save(category);
        if (!multipartFile.isEmpty()) categoryService.saveImage(multipartFile, savedCategory.getId());
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<String> saveImage(@RequestParam("image") MultipartFile multipartFile, @PathVariable(name = "id") Long id) throws IOException {
        categoryService.saveImage(multipartFile, id);
        return new ResponseEntity<>("Image has been saved successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@RequestBody Category category, @PathVariable(name = "id") Long id) {
        Category updatedCategory = categoryService.update(category, id);

        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) throws IOException {
        categoryService.delete(id);
        return new ResponseEntity<>("Category has been deleted successfully", HttpStatus.OK);
    }
}
