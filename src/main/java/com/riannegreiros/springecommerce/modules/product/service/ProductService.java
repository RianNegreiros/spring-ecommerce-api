package com.riannegreiros.springecommerce.modules.product.service;

import com.riannegreiros.springecommerce.modules.product.entity.Product;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    Product findByAlias(String alias);
    FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir);
    FindAllResponse findAllByKeyword(String keyword, Integer page, Integer size, String sortBy, String sortDir);
    FindAllResponse findAllByCategory(Long categoryId, Integer page, Integer size, String sortBy, String sortDir);    Product save (Product product);
    void updateEnabledStatus(Long id, boolean status);
    void delete(Long id) throws IOException;
    void saveImage(MultipartFile multipartFile, Long id) throws IOException;
    void saveExtraImages(MultipartFile[] multipartFiles, Long id) throws IOException;
    void saveProductDetails(String[] detailNames, String[] detailValues, Product product);
    void saveExistingImageNames(String[] imageIDs, String[] imageNames, Product product);
    void saveProductPrice(Product product);
}
