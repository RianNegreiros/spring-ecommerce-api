package com.riannegreiros.springecommerce.modules.category.repositoryTests;

import com.riannegreiros.springecommerce.modules.category.entity.Category;
import com.riannegreiros.springecommerce.modules.category.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CategoryRepositoryTests {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void testCreateRootCategory() {
        Category category = new Category("any_category");
        Category savedCategory = categoryRepository.save(category);

        assertThat(savedCategory.getId()).isEqualTo(category.getId());
    }

    @Test
    public void testCreateSubCategory() {
        Category parentCategory = new Category("any_category");
        Category subCategory = new Category("any_sub_category", parentCategory);
        Category savedCategory = categoryRepository.save(subCategory);

        assertThat(savedCategory.getId()).isEqualTo(subCategory.getId());
        assertThat(savedCategory.getParent()).isEqualTo(parentCategory);
    }
}
