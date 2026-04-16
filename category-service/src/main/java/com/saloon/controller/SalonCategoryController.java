package com.saloon.controller;

import com.saloon.entity.Category;
import com.saloon.payload.SalonDTO;
import com.saloon.service.CategoryService;
import com.saloon.service.client.SalonFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SalonCategoryController {

    private  final CategoryService categoryService;
    private  final SalonFeignClient salonFeignClient;

    @PostMapping("/categories/salon-owner")
    public ResponseEntity<Category> createCategory(@RequestBody Category category, @RequestHeader("Authorization") String jwt) throws Exception {
        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();
        Category saveCategory = categoryService.saveCategory(category, salonDTO);
        return ResponseEntity.ok(saveCategory);

    }

    @DeleteMapping("/categories/salon-owner/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id, @RequestHeader("Authorization") String jwt) throws Exception {
        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();
        categoryService.deleteCategoryById(id, salonDTO.getId());
        return ResponseEntity.ok("category deleted successfully");

    }

    @GetMapping("/categories/salon-owner/salon/{salonId}/category/{id}")
    public ResponseEntity<Category> getCategoryByIdAndSalon(@PathVariable Long id, @PathVariable Long salonId) throws Exception {
        Category category = categoryService.findByIdAndSalonId(id, salonId);
        return ResponseEntity.ok(category);

    }


}
