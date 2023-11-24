package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;


import java.util.Date;

import javax.persistence.Lob;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idCategory;
    private Long idBrand;
    private String name;
    private double price;
    private double priceSale;
    @Lob
    private byte[] imageData; 
    private Integer qty;
    private Date createdAt;
    private Date updatedAt;
    private String status;
    @Column(columnDefinition = "TEXT")
    private String description;
    

    public Product(String name, double price, byte[] imageData, Long idCategory, Long idBrand, double priceSale,String description,
            Integer qty, String status) {
        this.name = name;
        this.price = price;
        this.imageData = imageData;
        this.idCategory = idCategory;
        this.idBrand = idBrand;
        this.priceSale = priceSale;
        this.qty = qty;
        this.description=description;
   
        this.status = status ;
    }



   
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }

    public Long getIdBrand() {
        return idBrand;
    }

    public void setIdBrand(Long idBrand) {
        this.idBrand = idBrand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(double priceSale) {
        this.priceSale = priceSale;
    }

    public byte[] getImageData() { // Đổi tên phương thức và trường
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    @Column(name = "qty")
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
    	this.createdAt = new Date();

    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt =updatedAt ;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }


   
}
