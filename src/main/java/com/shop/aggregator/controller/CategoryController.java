package com.shop.aggregator.controller;

import com.shop.aggregator.exceptions.RestException;
import com.shop.aggregator.model.Category;
import com.shop.aggregator.repositories.CategoryProductRepository;
import com.shop.aggregator.repositories.CategoryRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/category")
public class CategoryController {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private CategoryProductRepository categoryProductRepository;
    
    private void validateCategory(Category category) throws RestException {
        if (category.getName() == null || category.getName().isEmpty()) {
            throw new RestException(400, "Category name not defined");
        }
    }
    
    @GetMapping
    @ResponseBody
    public List<Category> getAll() {
        return categoryRepository.findAll(CategoryRepository.DEFAULT_SORT);
    }
    
    @GetMapping("/{categoryId}")
    @ResponseBody
    public Category getById(@PathVariable("categoryId") UUID categoryId) throws RestException {
        Category category = categoryRepository.findOne(categoryId);
        if (category == null) {
            throw new RestException(404, "Category not found");
        }
        return category;
    }
    
    @PostMapping
    @ResponseBody
    public String create(@RequestBody Category category) throws RestException {
        validateCategory(category);
        if (categoryRepository.findByName(category.getName()) == null) {
            categoryRepository.save(category);
            return "Saved";
        } else {
            throw new RestException(400, "Category with same name already exists");
        }
    }
    
    @PutMapping("/{categoryId}")
    @ResponseBody
    public String update(@RequestBody Category category, @PathVariable("categoryId") UUID categoryId) throws RestException {
        validateCategory(category);
        Category entity = categoryRepository.findOne(categoryId);
        if (entity == null) {
            throw new RestException(404, "Category not found");
        }
        Category categoryWithSameName = categoryRepository.findByName(category.getName());
        if (categoryWithSameName != null && !entity.getId().equals(categoryWithSameName.getId())) {
            throw new RestException(400, "Category with same name already exists");
        }
        entity.setName(category.getName());
        categoryRepository.save(entity);
        return "Updated";
    }
    
    @DeleteMapping("/{categoryId}")
    @ResponseBody
    public String delete(@PathVariable("categoryId") UUID categoryId) throws RestException {
        if (!categoryRepository.exists(categoryId)) {
            throw new RestException(404, "Category not found");
        }
        categoryProductRepository.delete(categoryProductRepository.findByCategory(categoryId));
        categoryRepository.delete(categoryId);
        return "Deleted";
    }

}
