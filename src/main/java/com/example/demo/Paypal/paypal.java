package com.example.demo.Paypal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;



@Getter
@Entity
@Setter

@AllArgsConstructor
@NoArgsConstructor
public class paypal {
    
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    //  private Long id;

    // private String paymentId;
    // private String payerId;
	// private double amount;
    // private String idUser;
    //  private String idProduct;
	// private String currency;
	// private String method;
	// private String intent;
	// private String description;
    // private Date paymentTime;
    // private String status ;
       



	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne
	@JoinColumn(name = "payment_id")
     @JsonIgnore
	private PaymentInfo paymentInfo;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "quantity")
    private String quantity;

	@Column(name = "idUser")
    private String idUser;

    @Column(name = "amount")
    private String amount;


	@Column(name = "currency")
    private String currency;
   

    // @Column(name = "description")
    // private String description;

	

        
        
    
}
