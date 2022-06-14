package com.riannegreiros.springecommerce.modules.category.service.Impl;

import com.riannegreiros.springecommerce.AWS.StorageService;
import com.riannegreiros.springecommerce.modules.category.entity.Category;
import com.riannegreiros.springecommerce.modules.category.repository.CategoryRepository;
import com.riannegreiros.springecommerce.modules.category.service.CategoryService;
import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final StorageService storageService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, StorageService storageService) {
        this.categoryRepository = categoryRepository;
        this.storageService = storageService;
    }

    @Override
    public Category findCategory(String alias) {
        Category category = categoryRepository.findByAliasEnabled(alias);

        if (category == null) throw new ResourceNotFoundException("Category", "Alias", alias);
        if (!category.getEnabled()) throw new Error("Category is not enabled");

        return category;
    }

    @Override
    public FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Category> categories = categoryRepository.findAll(pageable);
        List<Category> categoryList = categories.getContent();

        return new FindAllResponse(categoryList,
                categories.getNumber(),
                categories.getSize(),
                categories.getTotalElements(),
                categories.getTotalPages(),
                categories.isLast());
    }

    @Override
    public FindAllResponse findAllRootCategories() {
        List<Category> categories = categoryRepository.findAllRootCategories();
        Page<Category> categoryPage = new PageImpl<>(categories);
        List<Category> categoryList = categoryPage.getContent();

        return new FindAllResponse(categoryList,
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryPage.isLast());
    }

    @Override
    public FindAllResponse findAllByKeyword(String keyword, Integer page, Integer size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Category> categoryPage = categoryRepository.findAllByKeyword(keyword, pageable);
        List<Category> categoryList = categoryPage.toList();

        return new FindAllResponse(categoryList,
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryPage.isLast());
    }

    @Override
    public FindAllResponse findAllEnabled() {
        List<Category> categories = categoryRepository.findAllByEnabled();
        Page<Category> categoryPage = new PageImpl<>(categories);
        List<Category> categoryList = categoryPage.getContent();

        return new FindAllResponse(categoryList,
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryPage.isLast());
    }

    @Override
    public void writeCategoriesToCSV(Writer writer) throws IOException {
        List<Category> categoryList = categoryRepository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("ID", "Name"))) {
            for (Category category : categoryList) {
                csvPrinter.printRecord(category.getId(), category.getName());
            }
        } catch (IOException ex) {
            throw new IOException("Could not save file." + ex);
        }
    }

    @Override
    public Category save(Category category) {
        categoryRepository.findByName(category.getName()).orElseThrow(() -> new Error("Category already exists with the name: " + category.getName()));
        String alias = category.getName().trim().replaceAll("/[^A-Za-z\\d]/", "-");
        category.setAlias(alias);
        category.setEnabled(true);
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category, Long id) {
        Category categoryExist = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("category", "id", id.toString()));

        categoryExist.setName(category.getName());
        categoryExist.setAlias(category.getAlias());
        categoryExist.setImage(category.getImage());
        if(categoryExist.getParent() != null) categoryExist.setParent(category.getParent());
        categoryExist.setChildren(category.getChildren());

        return categoryRepository.save(categoryExist);
    }

    @Override
    public void updateEnabledStatus(Long id, boolean status) {
        categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("category", "id", id.toString()));
        categoryRepository.updateEnabledStatus(id, status);
    }

    @Override
    public void delete(Long id) throws IOException {
        Category categoryExist = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("category", "id", id.toString()));
        storageService.deleteFile(categoryExist.getImagePath());
        categoryRepository.deleteById(id);
    }

    @Override
    public void saveImage(MultipartFile multipartFile, Long id) throws IOException {
        Category categoryExist = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("category", "id", id.toString()));
        String fileName = "categories-images/" + categoryExist.getId().toString() + multipartFile.getOriginalFilename();
        storageService.uploadFile(fileName, multipartFile);
        categoryExist.setImage(fileName);
    }

    @Override
    public byte[] findImage(Long id) throws IOException {
        Category categoryExist = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("category", "id", id.toString()));
        return storageService.downloadFile(categoryExist.getImagePath());
    }
}
