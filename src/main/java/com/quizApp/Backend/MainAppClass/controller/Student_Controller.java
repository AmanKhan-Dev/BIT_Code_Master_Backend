package com.quizApp.Backend.MainAppClass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import com.quizApp.Backend.MainAppClass.Service.Student_Service;
import com.quizApp.Backend.MainAppClass.model.Student;

import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;

@RequestMapping("student")
@RestController
public class Student_Controller {


@Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    Student_Service sservice;

    @GetMapping("/all")
    public Iterable<Student> getAllStudents(){
        return sservice.getAllStudents();
    }

    @PostMapping("/saveStudent")
    public ResponseEntity<Map<String, String>> saveStudent(@Valid @RequestBody Student student, BindingResult result) {
        Map<String, String> response = new HashMap<>();

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> {
                response.put(error.getObjectName(), error.getDefaultMessage());
            });
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Checking if email already exists
        if (sservice.existsByEmail(student.getEmail())) {
            response.put("email", "Email already exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Add the student if no error arfe there
        sservice.saveStudent(student);
        response.put("message", "Student saved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/logins")
    public Map<String, Object> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        
        boolean authenticated = sservice.authenticate(email, password);
        
        Map<String, Object> response = new HashMap<>();
        if (authenticated) {
            response.put("message","You are authenticated");
        } else {
            response.put("success", false);
            response.put("message", "Invalid email or password");
        }
        
        return response;
    }
}
