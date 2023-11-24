package com.example.demo.productrepons;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
}

