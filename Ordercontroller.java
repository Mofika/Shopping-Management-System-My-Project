package com.example.controller;

import com.example.model.Order;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class Ordercontroller {

    @Autowired
    private OrderService orderService;

    // Get all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Get an order by ID
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // Create a new order
    @PostMapping
    public Order createOrder(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        return orderService.createOrder(userId, productId, quantity);
    }

    // Delete an order
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}