package com.project3.storemanagement.service;

import com.project3.storemanagement.entities.Category;

import java.util.List;

public interface CategoryService {
    List<Category> listAllCategories();

    Category getCategoryById(Long id);

    Category saveCategory(Category category);
}
