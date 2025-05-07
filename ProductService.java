package com.example.service;

import com.example.model.Product;
import com.example.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get a product by ID
    public Product getProductById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    // Create a new product
    public Product createProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        return productRepository.save(product);
    }

    // Update a product
    public Product updateProduct(Long id, Product updatedProduct) {
        if (id == null || updatedProduct == null) {
            throw new IllegalArgumentException("Invalid input parameters");
        }

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());

        return productRepository.save(existingProduct);
    }

    // Delete a product
    public void deleteProduct(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }

        productRepository.deleteById(id);
    }
}