package com.riannegreiros.springecommerce.modules.product.service.Impl;

import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.category.repository.CategoryRepository;
import com.riannegreiros.springecommerce.modules.product.entity.Product;
import com.riannegreiros.springecommerce.modules.product.entity.ProductImage;
import com.riannegreiros.springecommerce.modules.product.repository.ProductRepository;
import com.riannegreiros.springecommerce.modules.product.service.ProductService;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
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
        Product productExists = productRepository.findByName(product.getName());

        if (productExists != null) {
            throw new ResourceNotFoundException("Product", "Name", product.getName());
        }

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
    public void updateEnabledStatus(Long id, boolean status) {
        productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "ID", id.toString()));

        productRepository.updateEnabledStatus(id, status);
    }

    @Override
    public void delete(Long id) throws IOException {
        Product productExist = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Brand", "ID", id.toString()));
        if (Files.exists(Path.of(productExist.getMainImagePath()))) Files.delete(Path.of(productExist.getMainImagePath()));
        if (Files.exists(Path.of(productExist.getMainImagePath()).getParent())) Files.delete(Path.of(productExist.getMainImagePath()).getParent());
        productRepository.delete(productExist);
    }

    @Override
    public void saveImage(MultipartFile multipartFile, Long id) throws IOException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));
        byte[] bytes = multipartFile.getBytes();
        Path path = Paths.get("/images/product-images/" + product.getId().toString() + multipartFile.getOriginalFilename());
        Files.write(path, bytes);
        product.setMainImage(path.toString());
    }

    @Override
    public void saveExtraImages(MultipartFile[] multipartFiles, Long id) throws IOException {
        Product productExist = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                Path path = Paths.get("/images/product-images/" + productExist.getId().toString() + multipartFile.getOriginalFilename());
                if (!productExist.containsImageName(path.toString())) productExist.addExtraImage(path.toString());
                byte[] bytes = multipartFile.getBytes();
                Files.write(path, bytes);
                productExist.addExtraImage(path.toString());
            }
        }
    }

    @Override
    public void saveProductDetails(String[] detailNames, String[] detailValues, Product product) {
        if (detailNames.length <= 0 || detailValues.length <= 0) return;

        for (int i = 0; i < detailNames.length; i++) {
            String name = detailNames[i];
            String value = detailValues[i];

            if (!name.isBlank() && !value.isBlank()) product.addDetail(name, value);
        }
    }

    @Override
    public void saveExistingImageNames(String[] imageIDs, String[] imageNames, Product product) {
        if (imageIDs.length <= 0 || imageNames.length <= 0) return;

        Set<ProductImage> images = new HashSet<>();

        for (int i = 0; i < imageIDs.length; i++) {
            Long id = Long.parseLong(imageIDs[i]);
            String name = imageNames[i];

            images.add(new ProductImage(id, name, product));
        }

        product.setImages(images);
    }

    @Override
    public void saveProductPrice(Product product) {
        Product productExist = productRepository.findById(product.getId()).orElseThrow(() -> new ResourceNotFoundException("user", "id", product.getId().toString()));
        productExist.setCost(product.getCost());
        productExist.setPrice(product.getPrice());
        productExist.setDiscountPercent(product.getDiscountPercent());

        productRepository.save(productExist);
    }
}
