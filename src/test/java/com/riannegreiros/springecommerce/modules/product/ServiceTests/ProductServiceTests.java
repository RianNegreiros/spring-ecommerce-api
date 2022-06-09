package com.riannegreiros.springecommerce.modules.product.ServiceTests;

import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.category.entity.Category;
import com.riannegreiros.springecommerce.modules.product.entity.Product;
import com.riannegreiros.springecommerce.modules.product.repository.ProductRepository;
import com.riannegreiros.springecommerce.modules.product.service.Impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    ProductRepository productRepository;
    ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    public void testSave() {
        Product newProduct = new Product();
        newProduct.setName("any_name");
        newProduct.setAlias("any name");
        newProduct.setShortDescription("any_description");
        newProduct.setFullDescription("any_description");
        productService.save(newProduct);

        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(productArgumentCaptor.capture());

        Product capturedProduct = productArgumentCaptor.getValue();

        assertThat(capturedProduct).isEqualTo(newProduct);
        assertThat(capturedProduct.getAlias()).isEqualTo(capturedProduct.getName().replaceAll(" ",  "-"));
        assertThat(capturedProduct.isEnabled()).isTrue();
        assertThat(capturedProduct.isInStock()).isTrue();
        assertThat(capturedProduct.getCreatedTime()).isNotNull();
        assertThat(capturedProduct.getUpdatedTime()).isNotNull();
    }

    @Test
    public void testThrowIfAlreadyExists() {
        Product product = new Product();
        product.setName("any_name");
        product.setAlias("any name");
        product.setShortDescription("any_description");
        product.setFullDescription("any_description");

        given(productRepository.findByName(anyString()))
                .willReturn(product);

        assertThatThrownBy(() -> productService.save(product))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product", "Name", product.getName());

        verify(productRepository, never()).save(any());
    }

    @Test
    public void testFindAll() {
        List<Product> categoryList = Arrays.asList(new Product(), new Product(), new Product());
        Page<Product> categoryPage = new PageImpl<>(categoryList);
        given(productRepository.findAll(any(Pageable.class)))
                .willReturn(categoryPage);

        productService.findAll(0, 10, "name", "asc");

        ArgumentCaptor<Pageable> valueArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(productRepository).findAll(valueArgumentCaptor.capture());

        Pageable capturedValue = valueArgumentCaptor.getValue();

        assertThat(capturedValue).isInstanceOf(Pageable.class);
        assertThat(capturedValue.getPageSize()).isEqualTo(10);
        assertThat(capturedValue.getPageNumber()).isEqualTo(0);
    }
}
