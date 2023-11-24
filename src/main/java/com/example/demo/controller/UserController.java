package com.example.demo.controller;


import java.util.List;


import org.hibernate.ResourceClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import com.example.demo.productrepons.UserRepository;
import com.example.demo.respone.ResourceNotFoundException;

import com.example.demo.Entity.User;
@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/users")
public class UserController {

	 @Autowired
	    private UserRepository userRepository;
	  @Autowired
	    private BCryptPasswordEncoder passwordEncoder;
	
	  @PostMapping("/add")
	    public ResponseEntity<User> addUser(
	            @RequestParam("name") String name,
	            @RequestParam("email") String email,
				@RequestPart("imageFile") MultipartFile imageFile,
				@RequestParam(value = "phone", required = false)String  phone,
	            @RequestParam("username") String username,
	            @RequestParam("password") String password,
	            @RequestParam("address") String  address ,
	     
	            @RequestParam("role") String  role ,
	            @RequestParam("status") String status
	            ) {
	        try {
	        	 
				byte[] imageData = imageFile.getBytes();
	        	  // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
	              String encryptedPassword = passwordEncoder.encode(password);

	            // Tạo mới sản phẩm và set các trường thông tin
	            User User = new User();
	            User.setName(name);
	            User.setEmail(email);
	            User.setPhone(phone);
				User.setImageData(imageData);
	            User.setUsername(username);
	            User.setPassword(encryptedPassword);
				User.setAddress(address);
	           
	            User.setRole(role);
	            User.setStatus(status);

	            // Lưu sản phẩm vào cơ sở dữ liệu
	            final User savedUser = userRepository.save(User);

	            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }
	  
	  

	    @GetMapping
	    public List<User> getAllUsers() {
	        return userRepository.findAll();
	    }

	    
	    
	    @GetMapping("/{id}")
	    public ResponseEntity<User> getProductById(@PathVariable Long id) {
	        User User = userRepository.findById(id)
	                .orElseThrow(() -> new ResourceClosedException("Product not found with id " + id));
	        return ResponseEntity.ok(User);
	    }

	 
	    
		@PutMapping("/{id}")
		public ResponseEntity<User> updateUser(
				@PathVariable Long id, @RequestBody User updateUser) {
			try {
				User existingUser = userRepository.findById(id)
						.orElseThrow(() -> new ResourceClosedException("Không tìm thấy người dùng với ID " + id));
	
				// Cập nhật thông tin người dùng với thông tin mới từ phần thân của request
				existingUser.setName(updateUser.getName());
				existingUser.setEmail(updateUser.getEmail());
				existingUser.setPhone(updateUser.getPhone());
				existingUser.setUsername(updateUser.getUsername());
				// Chỉ cập nhật mật khẩu nếu mật khẩu được cung cấp trong request
				if (updateUser.getPassword() != null) {
					String newEncryptedPassword = passwordEncoder.encode(updateUser.getPassword());
					existingUser.setPassword(newEncryptedPassword);
				}
				existingUser.setAddress(updateUser.getAddress());
				existingUser.setRole(updateUser.getRole());
				existingUser.setStatus(updateUser.getStatus());
	
				// Lưu thông tin người dùng đã cập nhật vào cơ sở dữ liệu
				final User updatedUser = userRepository.save(existingUser);
	
				return ResponseEntity.ok(updatedUser);
			} catch (ResourceNotFoundException e) {
				return ResponseEntity.notFound().build();
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}
	    
	    
	    
	    
	    @DeleteMapping("/{id}")
	    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
	        try {
	            User existingUser = userRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

	            // Xóa sản phẩm từ cơ sở dữ liệu
	            userRepository.delete(existingUser);

	            return ResponseEntity.ok("Product deleted successfully");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }
	
}
