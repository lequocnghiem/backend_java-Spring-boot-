package com.example.demo.productrepons;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Order;

@Repository
public interface OrderReponsi extends JpaRepository<Order, Long> {
     
}
