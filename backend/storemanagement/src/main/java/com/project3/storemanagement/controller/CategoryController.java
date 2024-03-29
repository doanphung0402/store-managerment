package com.project3.storemanagement.controller;

import com.project3.storemanagement.entities.Category;
import com.project3.storemanagement.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public Category findCategoryById(@PathVariable long id){
        return categoryService.getCategoryById(id);
    }

    @GetMapping
    public List<Category> findAllCategories(){
        return categoryService.listAllCategories();
    }

    @PostMapping
    public Category createCategory(@RequestBody @Valid Category category){
        return categoryService.saveCategory(category);
    }
}
