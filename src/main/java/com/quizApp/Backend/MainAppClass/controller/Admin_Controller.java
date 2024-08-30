package com.quizApp.Backend.MainAppClass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import com.quizApp.Backend.MainAppClass.Service.Admin_Service;
import com.quizApp.Backend.MainAppClass.model.Admin;


import java.util.HashMap;
import java.util.Map;

@RequestMapping("admin")
@RestController
public class Admin_Controller{

    @Autowired
    Admin_Service rservice;


    @PostMapping("/saveAdmin")
    public ResponseEntity<Map<String, String>> saveAdmin(@Valid @RequestBody Admin admin, BindingResult result) {
        Map<String, String> response = new HashMap<>();

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> {
                response.put(error.getObjectName(), error.getDefaultMessage());
            });
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Checking if email already exists
        if (rservice.existsByEmail(admin.getEmail())) {
            response.put("email", "Email already exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        
        rservice.saveAdmin(admin);
        response.put("message", "Admin saved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}