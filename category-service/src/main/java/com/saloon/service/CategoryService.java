package com.saloon.service;

import com.saloon.entity.Category;
import com.saloon.payload.SalonDTO;
import com.saloon.repository.CategoryRepository;
import com.saloon.service.impl.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private  final CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category, SalonDTO salonDTo) {
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        newCategory.setSalonId(salonDTo.getId());
        newCategory.setImages(category.getImages());
        return categoryRepository.save(newCategory);
    }

    @Override
    public Set<Category> getAllCategoriesBySalon(Long id) {
        return categoryRepository.getCategoriesBySalonId(id);
    }

    @Override
    public Category getCategoryById(Long id) throws Exception {
        Category category= categoryRepository.findById(id).orElse(null);
        if (category==null){
            throw  new Exception("category not exist with is" + id);
        }
        return category;
    }

    @Override
    public void deleteCategoryById(Long id, Long salonId) throws Exception {
       Category category = getCategoryById(id);
       if (!category.getSalonId().equals(salonId)){
           throw  new Exception("You don't have permission delete this category");
       }
       categoryRepository.deleteById(id);

    }

    @Override
    public Category findByIdAndSalonId(Long id, Long salonId) throws Exception {
        Category category = categoryRepository.findByIdAndSalonId(id, salonId);
        if (category==null){
            throw  new Exception("Category not found");
        }
        return  category;
    }
}
