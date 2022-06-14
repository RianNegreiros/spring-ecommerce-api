package com.riannegreiros.springecommerce.modules.brand.controller;

import com.riannegreiros.springecommerce.modules.brand.entity.Brand;
import com.riannegreiros.springecommerce.modules.brand.service.BrandService;
import com.riannegreiros.springecommerce.utils.AppConstants;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(value = "CRUD for brands resource", tags = {"Brand Controller"})
@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @ApiOperation(value = "Get all brands with given keyword with pagination")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public FindAllResponse findAll(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer size,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        if (keyword.isBlank()) return brandService.findAll(page, size, sortBy, sortDir);
        return brandService.findAllByKeyword(keyword, page, size, sortBy, sortDir);
    }

    @ApiOperation(value = "Create a new brand")
    @PostMapping
    public ResponseEntity<Brand> save(@RequestBody Brand brand) {
        Brand savedBrand = brandService.save(brand);
        return new ResponseEntity<>(savedBrand, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Upload brand image")
    @PostMapping("/image/{id}")
    public ResponseEntity<String> saveImage(@RequestParam("image") MultipartFile multipartFile, @PathVariable(name = "id") Long id) throws IOException {
        brandService.saveImage(multipartFile, id);
        return new ResponseEntity<>("Image has been saved successfully", HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update a brand")
    @PutMapping()
    public ResponseEntity<Brand> update(@RequestBody Brand brand) {
        brandService.update(brand);
        return new ResponseEntity<>(brand, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a brand")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) {
        brandService.delete(id);
        return new ResponseEntity<>("Brand has been deleted successfully", HttpStatus.OK);
    }
}
