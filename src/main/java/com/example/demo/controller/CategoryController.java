package com.example.demo.controller;



import java.util.Collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.ResourceClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


import com.example.demo.Entity.Category;

import com.example.demo.productrepons.CategoryRepository;
import com.example.demo.respone.ResourceNotFoundException;

@CrossOrigin(origins = "*")
@RestController

@RequestMapping("/categorys")
public class CategoryController {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @PostMapping("/add")
    public ResponseEntity<Category> addCategory(@RequestBody Category category ) {
        try {
            
            final Category savedCategory = categoryRepository.save(category);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping
    public List<Category> getAllCategorys() {
        return categoryRepository.findAll();
    }



    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceClosedException("Category not found with id " + id));
        return ResponseEntity.ok(category);
    }

  
    @PutMapping("/{id}")
public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category updateCategory) {
    try {
        Category existingCategory = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));

        existingCategory.setName(updateCategory.getName());
        existingCategory.setSlug(updateCategory.getSlug());
        existingCategory.setParent_id(updateCategory.getParent_id());
        existingCategory.setLevel(updateCategory.getLevel());
        existingCategory.setStatus(updateCategory.getStatus());

        // updatedAt sẽ được cập nhật tự động thông qua @PreUpdate trong Entity
        final Category updatedCategory = categoryRepository.save(existingCategory);

        return ResponseEntity.ok(updatedCategory);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}



 @DeleteMapping("/{id}")
public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable Long id) {
    try {
         Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
        // Lưu thông tin sản phẩm đã xóa
        Map<String, Object> response = new HashMap<>();
        response.put("id", existingCategory.getId()); // Đưa ID của sản phẩm vào phản hồi
        response.put("message", "Category deleted successfully");

        // Xóa sản phẩm từ cơ sở dữ liệu
          categoryRepository.delete(existingCategory);
        return ResponseEntity.ok(response);
    } catch (ResourceNotFoundException e) {
        // Xử lý nếu sản phẩm không tồn tại
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
    } catch (Exception e) {
        // Xử lý nếu có lỗi xảy ra trong quá trình xóa
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Failed to delete product"));
    }
}


    


}