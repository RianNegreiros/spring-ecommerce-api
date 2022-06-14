package com.riannegreiros.springecommerce.modules.category.repository;

import com.riannegreiros.springecommerce.modules.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    @Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
    List<Category> findAllRootCategories();
    @Query("SELECT c FROM Category c WHERE c.enabled = true AND c.alias = ?1")
    Category findByAliasEnabled(String alias);
    @Query("SELECT c FROM Category c WHERE c.name LIKE %?1% OR c.alias LIKE %?1%")
    Page<Category> findAllByKeyword(String keyword, Pageable pageable);
    @Query("SELECT c FROM Category c WHERE c.enabled = true ORDER BY c.name ASC")
    List<Category> findAllByEnabled();
    @Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
    @Modifying
    void updateEnabledStatus(Long id, boolean enabled);

}
