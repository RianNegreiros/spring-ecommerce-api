package com.riannegreiros.springecommerce.modules.brand.serviceTests;

import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.brand.entity.Brand;
import com.riannegreiros.springecommerce.modules.brand.repository.BrandRepository;
import com.riannegreiros.springecommerce.modules.brand.service.Impl.BrandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BrandServiceTests {

    @Mock
    BrandRepository brandRepository;
    BrandServiceImpl brandService;

    @BeforeEach
    void setUp() {
        brandService = new BrandServiceImpl(brandRepository);
    }

    @Test
    public void testSave() {
        Brand brand = new Brand("any_brand");
        brandService.save(brand);

        ArgumentCaptor<Brand> brandArgumentCaptor = ArgumentCaptor.forClass(Brand.class);

        verify(brandRepository).save(brandArgumentCaptor.capture());

        Brand categoryBrand = brandArgumentCaptor.getValue();

        assertThat(categoryBrand).isEqualTo(brand);
    }

    @Test
    public void testThrowIfExistsByName() {
        Brand brand = new Brand("any_brand");
        given(brandRepository.findByName(anyString()))
                .willReturn(Optional.of(brand));

        assertThatThrownBy(() -> brandService.save(brand))
                .isInstanceOf(Error.class)
                .hasMessageContaining("Brand already exists with this name: " + brand.getName());

        verify(brandRepository, never()).save(any());
    }

    @Test
    public void testUpdate() {
        Brand brand = new Brand("any_brand");
        Brand updatedBrand = new Brand();
        updatedBrand.setName("updated_name");
        updatedBrand.setLogo("updated_logo.png");

        given(brandRepository.findByName(any()))
                .willReturn(Optional.of(brand));

        brandService.update(updatedBrand);

        ArgumentCaptor<Brand> brandArgumentCaptor = ArgumentCaptor.forClass(Brand.class);

        verify(brandRepository).save(brandArgumentCaptor.capture());

        Brand capturedBrand = brandArgumentCaptor.getValue();

        assertThat(capturedBrand.getName()).isEqualTo(updatedBrand.getName());
        assertThat(capturedBrand.getLogo()).isEqualTo(updatedBrand.getLogo());
    }

    @Test
    public void testThrowIfDoNotExists() {
        Brand updatedBrand = new Brand();
        updatedBrand.setName("updated_name");
        updatedBrand.setLogo("updated_logo.png");

        given(brandRepository.findByName(anyString()))
                .willReturn(null);

        assertThatThrownBy(() -> brandService.update(updatedBrand))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Brand", "Name", updatedBrand.getName());

        verify(brandRepository, never()).save(any());
    }

    @Test
    public void testDelete() {
        Brand brand = new Brand("any_brand");

        given(brandRepository.findById(any()))
                .willReturn(Optional.of(brand));

        brandService.delete(1L);

        ArgumentCaptor<Brand> brandArgumentCaptor = ArgumentCaptor.forClass(Brand.class);

        verify(brandRepository, times(1)).delete(brandArgumentCaptor.capture());
    }
}
