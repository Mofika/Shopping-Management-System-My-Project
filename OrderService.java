package com.example.service;

import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repositories.OrderRepository;
import com.example.repositories.ProductRepository;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get an order by ID
    public Order getOrderById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
    }

    // Create a new order
    @Transactional
    public Order createOrder(Long userId, Long productId, int quantity) {
        validateInputParameters(userId, productId, quantity);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        validateProductStock(product, quantity);

        // Calculate total amount
        double totalAmount = calculateTotalAmount(product, quantity);

        // Update product quantity
        updateProductStock(product, quantity);

        // Create and save the order
        return saveOrder(user, product, totalAmount);
    }

    // Delete an order
    @Transactional
    public void deleteOrder(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }

        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with ID: " + id);
        }

        orderRepository.deleteById(id);
    }

    // Helper method to validate input parameters
    private void validateInputParameters(Long userId, Long productId, int quantity) {
        if (userId == null || productId == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid input parameters: userId, productId, and quantity must be provided");
        }
    }

    // Helper method to validate product stock
    private void validateProductStock(Product product, int quantity) {
        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }
    }

    // Helper method to calculate total amount
    private double calculateTotalAmount(Product product, int quantity) {
        return product.getPrice() * quantity;
    }

    // Helper method to update product stock
    private void updateProductStock(Product product, int quantity) {
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    // Helper method to save the order
    private Order saveOrder(User user, Product product, double totalAmount) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setTotalAmount(totalAmount);
        order.setUser(user);
        order.setProduct(product);
        return orderRepository.save(order);
    }
}