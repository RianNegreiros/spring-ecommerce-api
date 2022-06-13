package com.riannegreiros.springecommerce.modules.product.controller;

import com.riannegreiros.springecommerce.modules.product.entity.Product;
import com.riannegreiros.springecommerce.modules.product.service.ProductService;
import com.riannegreiros.springecommerce.security.userDetails.UserPrincipal;
import com.riannegreiros.springecommerce.utils.AppConstants;
import com.riannegreiros.springecommerce.utils.FileUploadUtil;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{alias}")
    public ResponseEntity<Product> findProduct(@PathVariable(name = "alias") String alias) {
        Product product = productService.findByAlias(alias);
        return new ResponseEntity<>(product, HttpStatus.FOUND);
    }

    @GetMapping
    public FindAllResponse findAllByKeyword(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer size,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        if (keyword.isBlank()) return productService.findAll(page, size, sortBy, sortDir);
        return productService.findAllByKeyword(keyword, page, size, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public FindAllResponse findAllByCategory(
            @PathVariable(value = "id", required = false) Long id,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer size,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return productService.findAllByCategory(id, page, size, sortBy, sortDir);
    }

    @PostMapping
    public ResponseEntity<Product> save(
            @RequestBody Product product,
            @RequestParam(value = "image",required = false) MultipartFile multipartFile,
            @RequestParam(value = "extraImages", required = false) MultipartFile[] multipartFiles,
            @RequestParam(value = "detailNames", required = false) String[] detailNames,
            @RequestParam(value = "detailValues", required = false) String[] detailValues,
            @RequestParam(value = "imageIDs", required = false) String[] imageIDs,
            @RequestParam(value = "imageNames", required = false) String[] imageNames
            ) throws IOException {
        if (!multipartFile.isEmpty()) productService.saveImage(multipartFile, product.getId());
        if (multipartFiles.length > 0) productService.saveExtraImages(multipartFiles, product.getId());
        productService.saveProductDetails(detailNames, detailValues, product);
        productService.saveExistingImageNames(imageIDs, imageNames, product);
        Product savedProduct = productService.save(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<String> save(@RequestParam("image") MultipartFile multipartFile, @PathVariable(name = "id") Long id) throws IOException {
        productService.saveImage(multipartFile, id);
        return new ResponseEntity<>("Image has been saved successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{id}/enabled/{status}")
    public ResponseEntity<String> updateEnabledStatus(@PathVariable(name = "id") Long id, @PathVariable(name = "status") Boolean status) {
        productService.updateEnabledStatus(id, status);
        return new ResponseEntity<>("Product status has been deleted successfully updated to: " + status, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) throws IOException {
        productService.delete(id);
        FileUploadUtil.deleteFile("/product-images/" + id);
        FileUploadUtil.deleteFile("/product-images/" + id + "/extras");
        return new ResponseEntity<>("Product has been deleted successfully", HttpStatus.OK);
    }
}
