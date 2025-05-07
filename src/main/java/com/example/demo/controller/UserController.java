// FILE: UserController.java
package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repo.FakeRepo;
import com.example.demo.service.UserService;
import com.example.demo.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final FakeRepo fakeRepo;

    @Autowired
    public UserController(UserService userService, FakeRepo fakeRepo) {
        this.userService = userService;
        this.fakeRepo = fakeRepo;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addUser(@RequestParam String name, @RequestParam String surname) {
        Map<String, Object> response = new HashMap<>();
        
        if (name == null || name.trim().isEmpty() || surname == null || surname.trim().isEmpty()) {
            response.put("error", "Name and surname cannot be empty");
            return ResponseEntity.badRequest().body(response);
        }
        
        userService.addUser(name, surname);
        response.put("message", name + " added successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable long id) {
        Map<String, Object> response = new HashMap<>();
        
        String result = fakeRepo.findUserById(id);
        if (result.equals("User not found")) {
            response.put("error", "User not found with ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        response.put("fullName", result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> removeUser(@PathVariable long id) {
        Map<String, Object> response = new HashMap<>();
        
        String result = fakeRepo.deleteUser(id);
        if (result.equals("User not found")) {
            response.put("error", "User not found with ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        response.put("message", result + " removed successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = fakeRepo.getAllUsers();
        return ResponseEntity.ok(users);
    }
}