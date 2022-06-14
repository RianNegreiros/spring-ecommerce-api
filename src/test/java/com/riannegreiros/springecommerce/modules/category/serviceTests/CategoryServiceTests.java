package com.riannegreiros.springecommerce.modules.category.serviceTests;

import com.riannegreiros.springecommerce.AWS.StorageService;
import com.riannegreiros.springecommerce.modules.category.entity.Category;
import com.riannegreiros.springecommerce.modules.category.repository.CategoryRepository;
import com.riannegreiros.springecommerce.modules.category.service.Impl.CategoryServiceImpl;
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
public class CategoryServiceTests {

    @Mock
    private CategoryRepository categoryRepository;
    private CategoryServiceImpl categoryService;

    private StorageService storageService;

    public CategoryServiceTests(StorageService storageService) {
        this.storageService = storageService;
    }

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository, storageService);
    }

    @Test
    public void testSave() {
        Category category = new Category("any_category");
        categoryService.save(category);

        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);

        verify(categoryRepository).save(categoryArgumentCaptor.capture());

        Category capturedCategory = categoryArgumentCaptor.getValue();

        assertThat(capturedCategory).isEqualTo(category);
    }

    @Test
    public void testSaveSubCategory() {
        Category parentCategory = new Category("any_category");
        Category subCategory = new Category("any_category", parentCategory);
        categoryService.save(subCategory);

        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);

        verify(categoryRepository).save(categoryArgumentCaptor.capture());

        Category capturedSubCategory = categoryArgumentCaptor.getValue();

        assertThat(capturedSubCategory).isEqualTo(subCategory);
    }

    @Test
    public void testThrowIfExistsByName() {
        Category category = new Category("any_category");
        given(categoryRepository.findByName(anyString()))
                .willReturn(Optional.of(category));

        assertThatThrownBy(() -> categoryService.save(category))
                .isInstanceOf(Error.class)
                .hasMessageContaining("Category already exists with this name: " + category.getName());

        verify(categoryRepository, never()).save(any());
    }

    @Test
    public void testUpdate() {
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

    @Test
    public void testRootStillNull() {
        Category parentCategory = new Category("parent_category");
        Category category = new Category("any_category");
        Category updateCategory = new Category();
        updateCategory.setName("updated_name");
        updateCategory.setParent(parentCategory);

        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(category));

        categoryService.update(updateCategory, category.getId());

        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);

        verify(categoryRepository).save(categoryArgumentCaptor.capture());

        Category capturedCategory = categoryArgumentCaptor.getValue();

        assertThat(capturedCategory.getParent()).isNull();
    }

    @Test
    public void testFindAll() {
        Category category = new Category("any_category");
        Category category1 = new Category("any_category");
        Category category2 = new Category("any_category");
        List<Category> categoryList = Arrays.asList(category, category1, category2);
        Page<Category> categoryPage = new PageImpl<>(categoryList);
        given(categoryRepository.findAll(any(Pageable.class)))
                .willReturn(categoryPage);

        categoryService.findAll(0, 10, "name", "asc");

        ArgumentCaptor<Pageable> valueArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(categoryRepository).findAll(valueArgumentCaptor.capture());

        Pageable capturedValue = valueArgumentCaptor.getValue();

        assertThat(capturedValue).isInstanceOf(Pageable.class);
        assertThat(capturedValue.getPageSize()).isEqualTo(10);
        assertThat(capturedValue.getPageNumber()).isEqualTo(0);
    }
}
