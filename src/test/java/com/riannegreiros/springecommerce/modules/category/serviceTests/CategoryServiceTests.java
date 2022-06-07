package com.riannegreiros.springecommerce.modules.category.serviceTests;

import com.riannegreiros.springecommerce.modules.category.entity.Category;
import com.riannegreiros.springecommerce.modules.category.repository.CategoryRepository;
import com.riannegreiros.springecommerce.modules.category.service.Impl.CategoryServiceImpl;
import com.riannegreiros.springecommerce.modules.user.entity.User;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {

    @Mock
    private CategoryRepository categoryRepository;
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    public void testSaveCategory() {
        Category category = new Category("any_category");
        categoryService.save(category);

        ArgumentCaptor<Category> userArgumentCaptor = ArgumentCaptor.forClass(Category.class);

        verify(categoryRepository).save(userArgumentCaptor.capture());

        Category capturedCategory = userArgumentCaptor.getValue();

        assertThat(capturedCategory).isEqualTo(category);
    }

    @Test
    public void testSaveSubCategory() {
        Category parentCategory = new Category("any_category");
        Category subCategory = new Category("any_category", parentCategory);
        categoryService.save(subCategory);

        ArgumentCaptor<Category> userArgumentCaptor = ArgumentCaptor.forClass(Category.class);

        verify(categoryRepository).save(userArgumentCaptor.capture());

        Category capturedSubCategory = userArgumentCaptor.getValue();

        assertThat(capturedSubCategory).isEqualTo(subCategory);
    }

    @Test
    public void testThrowIfCategoryExists() {
        Category category = new Category("any_category");
        given(categoryRepository.findByName(anyString()))
                .willReturn(category);

        assertThatThrownBy(() -> categoryService.save(category))
                .isInstanceOf(Error.class)
                .hasMessageContaining("Category already exists with this name: " + category.getName());

        verify(categoryRepository, never()).save(any());
    }

    @Test
    public void updateCategory() {
        Category category = new Category("any_category");
        Category updateCategory = new Category();
        updateCategory.setName("updated_name");
        updateCategory.setAlias("updated_alias");
        updateCategory.setImage("updated_image");

        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(category));

        categoryService.update(updateCategory, category.getId());

        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);

        verify(categoryRepository).save(categoryArgumentCaptor.capture());

        Category capturedCategory = categoryArgumentCaptor.getValue();

        assertThat(capturedCategory.getName()).isEqualTo(updateCategory.getName());
        assertThat(capturedCategory.getAlias()).isEqualTo(updateCategory.getAlias());
        assertThat(capturedCategory.getImage()).isEqualTo(updateCategory.getImage());
    }
}
