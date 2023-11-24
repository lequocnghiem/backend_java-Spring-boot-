package com.example.demo.productrepons;

import com.example.demo.Entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
	  Optional<User> findByimageData(byte[] imageData);
}
