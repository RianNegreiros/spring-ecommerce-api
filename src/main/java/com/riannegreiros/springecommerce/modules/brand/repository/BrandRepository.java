package com.riannegreiros.springecommerce.modules.brand.repository;

import com.riannegreiros.springecommerce.modules.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByName(String name);
}
