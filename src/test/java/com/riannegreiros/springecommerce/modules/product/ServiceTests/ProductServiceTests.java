package com.riannegreiros.springecommerce.modules.product.ServiceTests;

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
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
