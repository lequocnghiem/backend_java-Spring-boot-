package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;


import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.hibernate.ResourceClosedException;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.example.demo.Entity.Product;
import com.example.demo.productrepons.ProductRepository;
import com.example.demo.respone.ResourceNotFoundException;
;
@CrossOrigin(origins = "*")
@RestController

@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductRepository productRepository;
    
    @PostMapping("/add")
public ResponseEntity<Product> addProduct(@RequestBody Product product ) {
    try {
        

        
        final Product savedProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}


    
    

    
  

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceClosedException("Product not found with id " + id));
        return ResponseEntity.ok(product);
    }

  
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        try {
            Product existingProduct = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    
            // Cập nhật thông tin sản phẩm từ đối tượng updatedProduct
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            if (updatedProduct.getImageData() != null) {
                existingProduct.setImageData(updatedProduct.getImageData());
            }
            existingProduct.setIdCategory(updatedProduct.getIdCategory());
            existingProduct.setIdBrand(updatedProduct.getIdBrand());
            existingProduct.setPriceSale(updatedProduct.getPriceSale());
            existingProduct.setQty(updatedProduct.getQty());
            existingProduct.setStatus(updatedProduct.getStatus());
            existingProduct.setDescription(updatedProduct.getDescription());
    
            // Lưu sản phẩm đã cập nhật vào cơ sở dữ liệu
            final Product savedProduct = productRepository.save(existingProduct);
    
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    

    
   @DeleteMapping("/{id}")
public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id) {
    try {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        // Lưu thông tin sản phẩm đã xóa
        Map<String, Object> response = new HashMap<>();
        response.put("id", existingProduct.getId()); // Đưa ID của sản phẩm vào phản hồi
        response.put("message", "Product deleted successfully");

        // Xóa sản phẩm từ cơ sở dữ liệu
        productRepository.delete(existingProduct);

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
