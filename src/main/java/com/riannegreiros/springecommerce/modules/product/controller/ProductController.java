package com.riannegreiros.springecommerce.modules.product.controller;

import com.riannegreiros.springecommerce.modules.product.entity.Product;
import com.riannegreiros.springecommerce.modules.product.entity.ProductDetail;
import com.riannegreiros.springecommerce.modules.product.service.ProductService;
import com.riannegreiros.springecommerce.utils.AppConstants;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/{alias}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> findProduct(@PathVariable(name = "alias") String alias) {
        Product product = productService.findByAlias(alias);
        return new ResponseEntity<>(product, HttpStatus.FOUND);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public FindAllResponse findAllByCategory(
            @PathVariable(value = "id", required = false) Long id,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer size,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return productService.findAllByCategory(id, page, size, sortBy, sortDir);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(name = "id") Long id) throws IOException {
        byte[] data = productService.findImage(id);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment: filename=\"" + id + "\"")
                .body(resource);
    }

    @GetMapping(value = "/{id}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductDetail>> findProductDetails(@PathVariable(name = "id") Long id) {
        List<ProductDetail> productDetailList = productService.findAllProductDetails(id);
        return new ResponseEntity<>(productDetailList, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Product> save(
            @RequestBody Product product,
            @RequestParam(value = "image",required = false) MultipartFile multipartFile,
            @RequestParam(value = "extraImages", required = false) MultipartFile[] multipartFiles,
            @RequestParam(value = "detailNames", required = false) String[] detailNames,
            @RequestParam(value = "detailValues", required = false) String[] detailValues,
            @RequestParam(value = "imageIDs", required = false) String[] imageIDs,
            @RequestParam(value = "imageNames", required = false) String[] imageNames
            ) throws IOException {
        Product savedProduct = productService.save(product);
        if (!multipartFile.isEmpty()) productService.saveImage(multipartFile, savedProduct.getId());
        if (multipartFiles.length > 0) productService.saveExtraImages(multipartFiles, savedProduct.getId());
        productService.saveProductDetails(detailNames, detailValues, savedProduct.getId());
        productService.saveExistingImageNames(imageIDs, imageNames, savedProduct.getId());
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<String> save(@RequestParam("image") MultipartFile multipartFile, @PathVariable(name = "id") Long id) throws IOException {
        productService.saveImage(multipartFile, id);
        return new ResponseEntity<>("Image has been saved successfully", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/enabled/{status}")
    public ResponseEntity<String> updateEnabledStatus(@PathVariable(name = "id") Long id, @PathVariable(name = "status") Boolean status) {
        productService.updateEnabledStatus(id, status);
        return new ResponseEntity<>("Product status has been deleted successfully updated to: " + status, HttpStatus.OK);
    }

    @PatchMapping("/{id}/price/{price}")
    public ResponseEntity<String> updatePrice(@PathVariable(name = "id") Long id, @PathVariable(name = "price") Float price) {
        productService.updatePrice(id, price);
        return new ResponseEntity<>("Product price has been deleted successfully updated to: " + price.toString(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@RequestBody Product product, @PathVariable(name = "id") Long id) {
        Product updatedProduct = productService.update(product, id);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PostMapping("/{id}/detail")
    public ResponseEntity<Product> addDetail(@RequestBody ProductDetail productDetail, @PathVariable(name = "id") Long id) {
        Product updatedProduct = productService.addDetail(productDetail, id);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) throws IOException {
        productService.delete(id);
        return new ResponseEntity<>("Product has been deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}/detail")
    public ResponseEntity<String> deleteDetail(@PathVariable(name = "id") Long id) {
        productService.deleteDetail(id);
        return new ResponseEntity<>("Product Detail has been deleted successfully", HttpStatus.OK);
    }
}
