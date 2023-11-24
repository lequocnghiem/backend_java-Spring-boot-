package com.example.demo.Entity;


import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;




@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate; // Sử dụng LocalDateTime thay cho Date
    private Long idProduct;
    private String paymentMethod;
    private Long idUser;
    @Column(columnDefinition = "TEXT")
    private String shippingInfo;
    private String status;
    private Integer quantity;
    private double price;

    public Order(Long id, LocalDateTime orderDate, Long idProduct, String paymentMethod, Long idUser, String shippingInfo, String status, Integer quantity,double price) {
        this.id = id;
        this.orderDate = orderDate;
        this.idProduct = idProduct;
        this.paymentMethod = paymentMethod;
        this.idUser = idUser;
        this.shippingInfo = shippingInfo;
        this.status = status;
        this.quantity = quantity;
        this.price = price;
    }
    // Constructors, getters và setters
   


    // Các getters và setters

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }



    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
  


    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Order() {
        // Constructor mặc định không có tham số
    }


    @PrePersist
    public void prePersist() {
        this.orderDate = LocalDateTime.now();
    }

    // Các getters và setters cho các trường còn lại
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProduct() {
        return this.idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getIdUser() {
        return this.idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getShippingInfo() {
        return this.shippingInfo;
    }

    public void setShippingInfo(String shippingInfo) {
        this.shippingInfo = shippingInfo;
    }

    public LocalDateTime getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
