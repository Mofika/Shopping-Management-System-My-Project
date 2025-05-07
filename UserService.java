package com.example.service;

import com.example.model.User;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get a user by ID
    public User getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    // Create a new user
    public User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return userRepository.save(user);
    }

    // Update a user
    public User updateUser(Long id, User updatedUser) {
        if (id == null || updatedUser == null) {
            throw new IllegalArgumentException("Invalid input parameters");
        }

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        return userRepository.save(existingUser);
    }

    // Delete a user
    public void deleteUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }

        userRepository.deleteById(id);
    }
}