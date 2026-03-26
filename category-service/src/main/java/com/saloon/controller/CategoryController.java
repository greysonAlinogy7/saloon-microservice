package com.saloon.controller;

import com.saloon.entity.Category;
import com.saloon.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private  final CategoryService categoryService;



    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) throws Exception {
        Category categories = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categories);

    }

    @GetMapping("/salon/{id}")
    public ResponseEntity<Set<Category>> getCategoryBySalon(@PathVariable Long id) throws Exception {
        Set<Category> categories = categoryService.getAllCategoriesBySalon(id);
        return ResponseEntity.ok(categories);

    }




}
