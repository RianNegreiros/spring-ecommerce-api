package com.riannegreiros.springecommerce.modules.brand.repositoryTests;

import com.riannegreiros.springecommerce.modules.brand.entity.Brand;
import com.riannegreiros.springecommerce.modules.brand.repository.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BrandRepositoryTests {

    @Autowired
    BrandRepository brandRepository;

    private Brand brand;
    private Brand savedBrand;

    @BeforeEach
    void setUp() {
        brand = new Brand("any_brand");
        savedBrand = brandRepository.save(brand);
    }


    @Test
    public void testSaveBrand() {
        assertThat(savedBrand.getId()).isEqualTo(brand.getId());
    }

    @Test
    public void testFindByName() {
        Brand brandExist = brandRepository.findByName(brand.getName()).get();

        assertThat(brandExist).isNotNull();
        assertThat(brandExist.getName()).isEqualTo(brand.getName());
    }

    @Test
    public void testFindAllByKeyword() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Brand> brandPage = brandRepository.findAllByKeyword("any", pageable);

        assertThat(brandPage).isNotEmpty();
    }
}
