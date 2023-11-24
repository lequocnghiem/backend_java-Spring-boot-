
package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import com.example.demo.Entity.Order;

import com.example.demo.productrepons.OrderReponsi;

import com.example.demo.respone.ResourceNotFoundException;

@CrossOrigin(origins ="*")
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderReponsi orderRepository;
   

    @PostMapping("/add")
    public ResponseEntity<List<Order>> addMultipleOrders(@RequestBody List<Order> orders) {
        try {
            List<Order> savedOrders = new ArrayList<>();
            for (Order order : orders) {
                order.setOrderDate(LocalDateTime.now()); // Đặt ngày cho đơn hàng
                
                savedOrders.add(orderRepository.save(order));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        try {
            Order existingOrder = orderRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));

            // Cập nhật thông tin đơn hàng từ đối tượng updatedOrder
            existingOrder.setOrderDate(updatedOrder.getOrderDate());
            existingOrder.setIdProduct(updatedOrder.getIdProduct());
            existingOrder.setPaymentMethod(updatedOrder.getPaymentMethod());
            existingOrder.setIdUser(updatedOrder.getIdUser());
            existingOrder.setShippingInfo(updatedOrder.getShippingInfo());
           existingOrder.setStatus(updatedOrder.getStatus());
            existingOrder.setQuantity(updatedOrder.getQuantity());
            // Lưu đơn hàng đã cập nhật vào cơ sở dữ liệu
            final Order savedOrder = orderRepository.save(existingOrder);

            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable Long id) {
        try {
            Order existingOrder = orderRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));

            // Lưu thông tin đơn hàng đã xóa
            Map<String, Object> response = new HashMap<>();
            response.put("id", existingOrder.getId()); // Đưa ID của đơn hàng vào phản hồi
            response.put("message", "Order deleted successfully");

            // Xóa đơn hàng từ cơ sở dữ liệu
            orderRepository.delete(existingOrder);

            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            // Xử lý nếu đơn hàng không tồn tại
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            // Xử lý nếu có lỗi xảy ra trong quá trình xóa
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Failed to delete order"));
        }
    }
}
