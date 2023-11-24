package com.example.demo.Paypal;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Entity
@Setter

@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "payer_id")
    private String payerId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "idUser")
    private String idUser;

    @Column(name = "currency")
    private String currency;

     @Column(name = "intent")
  private String intent;

     @Column(name = "method")
   private String method;


    @Column(name = "payment_time")
    private Date paymentTime;

  @Column(name = "status")
     private String status ;


     @OneToMany(mappedBy = "paymentInfo", cascade = CascadeType.ALL, orphanRemoval = true)
      @JsonIgnore
     private List<paypal> paypalList;

}
