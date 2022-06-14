package com.riannegreiros.springecommerce.modules.product.service.Impl;

import com.riannegreiros.springecommerce.AWS.StorageService;
import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.category.repository.CategoryRepository;
import com.riannegreiros.springecommerce.modules.product.entity.Product;
import com.riannegreiros.springecommerce.modules.product.entity.ProductDetail;
import com.riannegreiros.springecommerce.modules.product.entity.ProductImage;
import com.riannegreiros.springecommerce.modules.product.repository.ProductRepository;
import com.riannegreiros.springecommerce.modules.product.service.ProductService;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final StorageService storageService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, StorageService storageService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.storageService = storageService;
    }

    @Override
    public Product findByAlias(String alias) {
        Product product = productRepository.findByAliasEnabled(alias);

        if (product == null) throw new ResourceNotFoundException("Product", "Alias", alias);
        if (!product.isEnabled()) throw new Error("Product is not enabled");

        return product;
    }

    @Override
    public FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productRepository.findAll(pageable);
        List<Product> productList = products.getContent();

        return new FindAllResponse(productList,
                products.getNumber(),
                products.getSize(),
                products.getTotalElements(),
                products.getTotalPages(),
                products.isLast());
    }

    @Override
    public FindAllResponse findAllByKeyword(String keyword, Integer page, Integer size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productRepository.findAll(keyword, pageable);
        List<Product> productList = productPage.toList();

        return new FindAllResponse(productList,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast());
    }

    @Override
    public FindAllResponse findAllByCategory(Long categoryId, Integer page, Integer size, String sortBy, String sortDir) {
        categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId.toString()));
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productRepository.findAllByCategory(categoryId, pageable);
        List<Product> productList = productPage.toList();

        return new FindAllResponse(productList,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast());
    }

    @Override
    public Product save(Product product) {
        productRepository.findByName(product.getName()).orElseThrow(() -> new ResourceNotFoundException("Product", "Name", product.getName()));

        if (product.getAlias().isBlank()) {
            String defaultAlias = product.getName().replaceAll(" ",  "-");
            product.setAlias(defaultAlias);
        }

        product.setAlias(product.getName().replaceAll(" ",  "-"));
        product.setEnabled(true);
        product.setInStock(true);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        return productRepository.save(product);
    }

    @Override
    public List<ProductDetail> findAllProductDetails(Long id) {
        Product productExist = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));
        return productExist.getDetails();
    }

    @Override
    public void updateEnabledStatus(Long id, boolean status) {
        productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "ID", id.toString()));

        productRepository.updateEnabledStatus(id, status);
    }

    @Override
    public Product update(Product product, Long id) {
        Product productExist = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));
        productExist.setName(product.getName());
        productExist.setAlias(productExist.getAlias());
        productExist.setFullDescription(product.getFullDescription());
        productExist.setShortDescription(product.getShortDescription());
        productExist.setInStock(product.isInStock());
        productExist.setCost(product.getCost());
        productExist.setPrice(product.getPrice());
        productExist.setDiscountPercent(product.getDiscountPercent());
        productExist.setLength(product.getLength());
        productExist.setWidth(product.getWidth());
        productExist.setWeight(product.getWeight());
        productExist.setCategory(product.getCategory());
        productExist.setBrand(product.getBrand());
        return productRepository.save(productExist);
    }

    @Override
    public Product addDetail(ProductDetail productDetail, Long id) {
        Product productExist = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));
        productExist.getDetails().add(productDetail);
        return productRepository.save(productExist);
    }

    @Override
    public void deleteDetail(Long id) {
        productRepository.deleteProductDetail(id);
    }

    @Override
    public void delete(Long id) throws IOException {
        Product productExist = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Brand", "ID", id.toString()));
        storageService.deleteFile(productExist.getMainImagePath());
        productExist.getImages().forEach(image -> storageService.deleteFile(image.getName()));
        productRepository.delete(productExist);
    }

    @Override
    public void saveImage(MultipartFile multipartFile, Long id) throws IOException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));
        String fileName = "product-images/" + product.getId().toString() + multipartFile.getOriginalFilename();
        storageService.uploadFile(fileName, multipartFile);
        product.setMainImage(fileName);
    }

    @Override
    public void saveExtraImages(MultipartFile[] multipartFiles, Long id) throws IOException {
        Product productExist = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                String fileName = "product-images/" + productExist.getId().toString() + multipartFile.getOriginalFilename();
                storageService.uploadFile(fileName, multipartFile);
                productExist.addExtraImage(fileName);
            }
        }
    }

    @Override
    public void saveProductDetails(String[] detailNames, String[] detailValues, Long id) {
        Product productExist = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));
        if (detailNames.length <= 0 || detailValues.length <= 0) return;

        for (int i = 0; i < detailNames.length; i++) {
            String name = detailNames[i];
            String value = detailValues[i];

            if (!name.isBlank() && !value.isBlank()) productExist.addDetail(name, value);
        }
    }

    @Override
    public void saveExistingImageNames(String[] imageIDs, String[] imageNames, Long id) {
        Product productExist = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));
        if (imageIDs.length <= 0 || imageNames.length <= 0) return;

        Set<ProductImage> images = new HashSet<>();

        for (int i = 0; i < imageIDs.length; i++) {
            Long imageId = Long.parseLong(imageIDs[i]);
            String name = imageNames[i];

            images.add(new ProductImage(imageId, name, productExist));
        }

        productExist.setImages(images);
    }

    @Override
    public void updatePrice(Long id, Float price) {
        Product productExist = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("category", "id", id.toString()));
        productExist.setPrice(price);
    }

    @Override
    public byte[] findImage(Long id) throws IOException {
        Product productExist = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("category", "id", id.toString()));
        return storageService.downloadFile(productExist.getMainImage());
    }
}
