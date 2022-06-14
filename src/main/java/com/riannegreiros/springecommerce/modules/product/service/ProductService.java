package com.riannegreiros.springecommerce.modules.product.service;

import com.riannegreiros.springecommerce.modules.product.entity.Product;
import com.riannegreiros.springecommerce.modules.product.entity.ProductDetail;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    Product findByAlias(String alias);
    FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir);
    FindAllResponse findAllByKeyword(String keyword, Integer page, Integer size, String sortBy, String sortDir);
    FindAllResponse findAllByCategory(Long categoryId, Integer page, Integer size, String sortBy, String sortDir);
    Product save (Product product);
    void updatePrice(Long id, Float price);
    Product update(Product product, Long id);
    void updateEnabledStatus(Long id, boolean status);
    void delete(Long id) throws IOException;
    byte[] findImage(Long id) throws IOException;
    void saveImage(MultipartFile multipartFile, Long id) throws IOException;
    void saveExtraImages(MultipartFile[] multipartFiles, Long id) throws IOException;
    void saveProductDetails(String[] detailNames, String[] detailValues, Long id);
    void saveExistingImageNames(String[] imageIDs, String[] imageNames, Long id);
    List<ProductDetail> findAllProductDetails(Long id);
    Product addDetail(ProductDetail productDetail, Long id);
    void deleteDetail(Long id);
}
