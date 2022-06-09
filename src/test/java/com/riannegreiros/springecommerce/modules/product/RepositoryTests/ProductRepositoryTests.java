package com.riannegreiros.springecommerce.modules.product.RepositoryTests;

import com.riannegreiros.springecommerce.modules.product.entity.Product;
import com.riannegreiros.springecommerce.modules.product.entity.ProductDetail;
import com.riannegreiros.springecommerce.modules.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    ProductRepository productRepository;

    private Product product;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setName("any_name");
        product.setAlias("any_alias");
        product.setShortDescription("any_description");
        product.setFullDescription("any_description");
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());
        product.setEnabled(true);
        product.setInStock(true);
        product.setCost(999F);
        product.setPrice(999F);
        product.setDiscountPercent(99F);
        product.setLength(99F);
        product.setWeight(99F);
        product.setHeight(99F);
        product.setWeight(99F);

        savedProduct = productRepository.save(product);
    }

    @Test
    public void testCreate() {
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct).isEqualTo(product);
    }

    @Test
    public void testFindAll() {
        List<Product> productList = productRepository.findAll();

        assertThat(productList).hasAtLeastOneElementOfType(Product.class);
    }

    @Test
    public void testUpdate() {
        Product findProduct = productRepository.findById(savedProduct.getId()).get();

        findProduct.setName("updated_name");
        findProduct.setAlias("updated_alias");
        findProduct.setUpdatedTime(new Date());

        Product updatedProduct = productRepository.save(findProduct);

        assertThat(product.getName()).isEqualTo(updatedProduct.getName());
        assertThat(product.getAlias()).isEqualTo(updatedProduct.getAlias());
        assertThat(product.getUpdatedTime()).isEqualTo(updatedProduct.getUpdatedTime());
    }

    @Test
    public void testDelete() {
        productRepository.deleteById(savedProduct.getId());

        boolean findProduct = productRepository.existsById(product.getId());

        assertThat(findProduct).isFalse();
    }

    @Test
    public void testSaveImages() {
        savedProduct.setMainImage("main-image.jpg");
        savedProduct.addExtraImage("extra-image.png");
        savedProduct.addExtraImage("extra-image1.png");

        Product updatedProduct = productRepository.save(savedProduct);

        assertThat(updatedProduct.getImages()).isNotEmpty();
        assertThat(updatedProduct.getImages()).hasSize(2);
    }

    @Test
    public void testAddDetails() {
        savedProduct.addDetail("any_name", "any_value");
        savedProduct.addDetail("any_name", "any_value");
        savedProduct.addDetail("any_name", "any_value");

        Product updatedProduct = productRepository.save(savedProduct);

        assertThat(updatedProduct.getDetails()).isNotEmpty();
        assertThat(updatedProduct.getDetails()).hasSize(3);
        assertThat(updatedProduct.getDetails()).hasAtLeastOneElementOfType(ProductDetail.class);
    }
}
