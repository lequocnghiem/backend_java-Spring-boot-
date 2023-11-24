package com.example.demo.Paypal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import com.example.demo.respone.ResourceNotFoundException;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
@CrossOrigin(origins = "*")
@RestController

@RequestMapping("/api") 
public class PaypalResource {

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private PayPalReponsi payPalReponsi;


   @Autowired
    private paymentInfoRepository paymentInfoRepository;



//hamf add 
@PostMapping("/add")
public ResponseEntity<?> createPayment( @RequestBody List<Payments> Payments) {
    try {
        Payment payment = paypalService.createPayment(Payments, "http://localhost:9090/8080/cancel", "http://172.20.10.5:9097/api/success/pay");

        System.out.println("Phản hồi thanh toán: " + payment.toJSON());

        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", UUID.randomUUID().toString()); // Tạo ID mới cho mỗi thanh toán
                data.put("approvalUrl", link.getHref());
                return ResponseEntity.ok(data);
            }
        }
    } catch (PayPalRESTException e) {
        e.printStackTrace();
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Error creating payment.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    Map<String, Object> notFoundResponse = new HashMap<>();
    notFoundResponse.put("error", "Payment approval URL not found.");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(notFoundResponse);
}


    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPay() {
        return ResponseEntity.ok("Payment canceled.");
    }

    


  
    public ModelAndView pay() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pay"); // Giả sử "pay" là tên của template HTML của bạn
        // Thêm bất kỳ dữ liệu nào bạn muốn truyền vào view
        modelAndView.addObject("key", "value");
        return modelAndView;
    }

    @GetMapping("/success/pay")
    public ModelAndView successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                savePaymentToDatabase(payment); // Truyền danh sách orders vào phương thức savePaymentToDatabase
                modelAndView.setViewName("pay"); // Đặt tên view thành "pay"
                modelAndView.addObject("paymentId", paymentId);
                return modelAndView;
            }
        } catch (PayPalRESTException e) {
            // Xử lý lỗi
            modelAndView.setViewName("error"); // Đặt tên view thành "error" nếu có lỗi PayPalRESTException
            return modelAndView;
        }
        modelAndView.setViewName("error"); // Đặt tên view thành "error" nếu thanh toán không được chấp nhận
        return modelAndView;
    }

    private void savePaymentToDatabase(Payment payment) {
        // Lấy thông tin từ đối tượng Payment và lưu vào cơ sở dữ liệu
        PaymentInfo paymentInfoEntity = new PaymentInfo();
        paymentInfoEntity.setPaymentId(payment.getId());
        paymentInfoEntity.setPayerId(payment.getPayer().getPayerInfo().getPayerId());
        paymentInfoEntity.setAmount(Double.parseDouble(String.valueOf(payment.getTransactions().get(0).getAmount().getTotal())));
        paymentInfoEntity.setStatus(payment.getPayer().getStatus());
          paymentInfoEntity.setIdUser(payment.getTransactions().get(0).getDescription());
        paymentInfoEntity.setMethod(payment.getPayer().getPaymentMethod());
        paymentInfoEntity.setIntent(payment.getIntent());
        paymentInfoEntity.setCurrency(payment.getTransactions().get(0).getAmount().getCurrency());
        paymentInfoEntity.setPaymentTime(new Date()); // Thời gian thanh toán
    
        PaymentInfo savedPaymentInfo = paymentInfoRepository.save(paymentInfoEntity);
    
        // Lấy thông tin chi tiết thanh toán từ đối tượng Payment và lưu vào cơ sở dữ liệu
        for (Transaction transaction : payment.getTransactions()) {
            for (Item item : transaction.getItemList().getItems()) {
                paypal paymentEntity = new paypal();
                paymentEntity.setPaymentInfo(savedPaymentInfo);
                paymentEntity.setProductId(item.getName());
                paymentEntity.setIdUser(item.getDescription());
                paymentEntity.setQuantity(item.getQuantity());
                paymentEntity.setAmount(item.getPrice());
                paymentEntity.setCurrency(item.getCurrency());
                payPalReponsi.save(paymentEntity);
            }
        }
    
}


   
   

    @GetMapping
    public List<paypal> getAllCategorys() {
        return payPalReponsi.findAll();
    }




     @DeleteMapping("/{id}")
public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable Long id) {
    try {
         paypal existingCategory = payPalReponsi.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
        // Lưu thông tin sản phẩm đã xóa
        Map<String, Object> response = new HashMap<>();
        response.put("id", existingCategory.getId()); // Đưa ID của sản phẩm vào phản hồi
        response.put("message", "Category deleted successfully");

        // Xóa sản phẩm từ cơ sở dữ liệu
          payPalReponsi.delete(existingCategory);
        return ResponseEntity.ok(response);
    } catch (ResourceNotFoundException e) {
        // Xử lý nếu sản phẩm không tồn tại
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
    } catch (Exception e) {
        // Xử lý nếu có lỗi xảy ra trong quá trình xóa
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Failed to delete product"));
    }
}

}
