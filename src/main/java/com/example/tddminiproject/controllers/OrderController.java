package com.example.tddminiproject.controllers;

import com.example.tddminiproject.models.Order;
import com.example.tddminiproject.repositories.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {

        Order myOrder = null;
        Optional<Order> myOrderOpt = orderRepository.findById(Long.valueOf(orderId));
        if (myOrderOpt.isPresent()) {
            myOrder = myOrderOpt.get();
            return ResponseEntity.status(HttpStatus.OK).body(myOrder);

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Object> updateOrder(@PathVariable String orderId, @Valid @RequestBody Order updatedOrder) {
        Optional<Order> existingOrderOpt = orderRepository.findById(Long.valueOf(orderId));
        if (existingOrderOpt.isEmpty()) {
            throw new RuntimeException("Order with ID " + orderId + " not found.");
        }

        Order existingOrder = existingOrderOpt.get();
        existingOrder.setCustomerName(updatedOrder.getCustomerName());
        existingOrder.setOrderDate(updatedOrder.getOrderDate());
        existingOrder.setShippingAddress(updatedOrder.getShippingAddress());
        existingOrder.setTotal(updatedOrder.getTotal());

        Order savedOrder = orderRepository.save(existingOrder);

        return ResponseEntity.ok(savedOrder);
    }


    @PostMapping("/create")
    public ResponseEntity<Object> createOrder(@Valid @RequestBody Order order, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();


            List<String> errorMessages = new ArrayList<>();
            for (FieldError error : errors) {
                errorMessages.add(error.getDefaultMessage());
            }


            return ResponseEntity.badRequest().body(errorMessages);
        }


        Order savedOrder = orderRepository.save(order);


        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Object> deleteOrder(@PathVariable String orderId) {
        Optional<Order> existingOrderOpt = orderRepository.findById(Long.valueOf(orderId));
        if (existingOrderOpt.isEmpty()) {
            throw new RuntimeException("Order with ID " + orderId + " not found.");
        }

        orderRepository.delete(existingOrderOpt.get());

        return ResponseEntity.noContent().build();
    }
}
