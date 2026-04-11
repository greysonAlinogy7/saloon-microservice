package com.saloon.service.impl;

import com.saloon.entity.Category;
import com.saloon.payload.SalonDTO;

import java.util.List;
import java.util.Set;

public interface ICategoryService {
    Category saveCategory(Category category, SalonDTO salonDTo);
    Set<Category> getAllCategoriesBySalon(Long id);
    Category getCategoryById(Long id) throws Exception;
    void deleteCategoryById(Long id, Long salonId) throws Exception;
    Category findByIdAndSalonId(Long id, Long salonId) throws Exception;
}
