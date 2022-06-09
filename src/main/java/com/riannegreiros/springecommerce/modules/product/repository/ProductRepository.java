package com.riannegreiros.springecommerce.modules.product.repository;

import com.riannegreiros.springecommerce.modules.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);

    @Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
    @Modifying
    void updateEnabledStatus(Long id, boolean enabled);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% " +
            "OR p.shortDescription LIKE %?1%" +
            "OR p.fullDescription LIKE %?1%" +
            "OR p.brand.name LIKE %?1%" +
            "OR p.category.name LIKE %?1%")
    Page<Product> findAll(String keyword, Pageable pageable);
}
