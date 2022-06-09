package com.riannegreiros.springecommerce.modules.product.service;

import com.riannegreiros.springecommerce.modules.product.entity.Product;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir);
    Product save (Product product);
    void updateEnabledStatus(Long id, boolean status);
    void delete(Long id);
    void saveImage(MultipartFile multipartFile, Long id) throws IOException;
    void saveExtraImages(MultipartFile[] multipartFiles, Long id) throws IOException;
    void saveProductDetails(String[] detailNames, String[] detailValues, Long id);
}
