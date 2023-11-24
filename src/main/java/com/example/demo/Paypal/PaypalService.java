package com.example.demo.Paypal;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;



@Service
public class PaypalService {
	
	@Autowired
	private APIContext apiContext;
	
	public Payment createPayment(List<Payments> payments, String cancelUrl, String successUrl) throws PayPalRESTException {
        double totalAmount = 0.0;



        List<Item> items = new ArrayList<>();

        for (Payments payment : payments) {
           
            BigDecimal itemTotal = new BigDecimal(payment.getAmount()).setScale(2, RoundingMode.HALF_UP);

            // Nhân số lượng với giá tiền để có tổng giá trị của sản phẩm
            int quantity = Integer.parseInt(payment.getQuantity());

            // Nhân số lượng với giá tiền để có tổng giá trị của sản phẩm
            itemTotal = itemTotal.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);

   BigDecimal totalBigDecimal = new BigDecimal(payment.getAmount()).setScale(2, RoundingMode.HALF_UP);
           
            Item item = new Item();
            item.setName(payment.getIdProduct());
            item.setCurrency(payment.getCurrency());
            item.setPrice(totalBigDecimal.toString());
            item.setQuantity(payment.getQuantity());
          
            // Thêm item vào danh sách items
            item.setDescription(payment.getIdUser());
            items.add(item);




            totalAmount += itemTotal.doubleValue();

            System.out.println("Product: " + payment.getIdProduct() + ", Quantity: " + quantity + ", Item Total: " + itemTotal);
           
        }



ItemList itemList = new ItemList();
    itemList.setItems(items);


  



        // Tổng thanh toán cho tất cả sản phẩm
        Amount totalAmountObj = new Amount();
        totalAmountObj.setCurrency(payments.get(0).getCurrency());
        BigDecimal totalBigDecimal = new BigDecimal(totalAmount).setScale(2, RoundingMode.HALF_UP);
        totalAmountObj.setTotal(totalBigDecimal.toString());
        
 System.out.println("Total Amount: " + totalAmountObj);

        Transaction totalTransaction = new Transaction();
        totalTransaction.setItemList(itemList);
        totalTransaction.setDescription( payments.get(0).getIdUser());
        totalTransaction.setAmount(totalAmountObj);

        System.out.println("totalTransaction " + totalTransaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(payments.get(0).getMethod().toString());
        payer.setStatus(payments.get(0).getStatus());


        Payment payment = new Payment();
        payment.setIntent(payments.get(0).getIntent().toString());
        payment.setPayer(payer);
        payment.setTransactions(Collections.singletonList(totalTransaction));



        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        payment.setRedirectUrls(redirectUrls);
        apiContext.setMaskRequestId(true);

        String requestId = apiContext.getRequestId();
        System.out.println("RequestId for the recent request: " + requestId);

        return payment.create(apiContext);
    } 






public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
    Payment payment = new Payment();
    payment.setId(paymentId);

  
    

    PaymentExecution paymentExecute = new PaymentExecution();
    paymentExecute.setPayerId(payerId);

    return payment.execute(apiContext, paymentExecute);
}


}
