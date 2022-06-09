package com.riannegreiros.springecommerce.modules.product.service.Impl;

import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.brand.entity.Brand;
import com.riannegreiros.springecommerce.modules.product.entity.Product;
import com.riannegreiros.springecommerce.modules.product.repository.ProductRepository;
import com.riannegreiros.springecommerce.modules.product.service.ProductService;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Brand", "ID", id.toString()));
        productRepository.delete(product);
    }
}
