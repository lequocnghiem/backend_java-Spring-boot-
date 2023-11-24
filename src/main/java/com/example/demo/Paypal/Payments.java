package com.example.demo.Paypal;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@Getter
@Entity
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payments {
    

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

    private String paymentId;
    private String payerId;
	private double amount;
    private String idUser;
    private String idProduct;
	private String currency;
	private String method;
    private String quantity;
	private String intent;
	private String description;
    private Date paymentTime;
    private String status ;
}
