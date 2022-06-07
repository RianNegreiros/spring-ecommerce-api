package com.riannegreiros.springecommerce.modules.category.repository;

import com.riannegreiros.springecommerce.modules.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
    List<Category> findAllRootCategories();
    Category findByName(String name);
}
