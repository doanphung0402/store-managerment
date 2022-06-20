package com.project3.storemanagement.service.impl;

import com.project3.storemanagement.entities.Category;
import com.project3.storemanagement.service.CategoryService;
import com.project3.storemanagement.exception.BadNumberException;
import com.project3.storemanagement.exception.RecordNotFoundException;
import com.project3.storemanagement.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> listAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        if(id <= 0) {
            throw new BadNumberException("id must be greater than 0");
        }

        return categoryRepository
            .findById(id)
            .orElseThrow(() -> new RecordNotFoundException("Category not found"));
    }

    @Override
    @Transactional
    public Category saveCategory(Category category) {
        Category newCategory = new Category(
                StringUtils.capitalize(category.getName()),
                category.getDescription()
        );
        return categoryRepository.save(newCategory);
    }
}
