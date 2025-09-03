package com.quizApp.Backend.MainAppClass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.Optional;



@RequestMapping("/student")
@RestController
public class Student_Controller {



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

        if (sservice.existsByEmail(student.getEmail())) {
            response.put("email", "Email already exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        sservice.saveStudent(student);
        response.put("message", "Student saved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
@GetMapping("/findByEmail")
    public ResponseEntity<Student> findStudentByEmail(@RequestParam String email) {
        Optional<Student> studentOptional = sservice.findStudentByEmail(email);
        return studentOptional.map(ResponseEntity::ok)
                              .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/logins")
public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> payload) {
    String email = payload.get("email");
    String password = payload.get("password");
    
    boolean authenticated = sservice.authenticate(email, password);
    
    Map<String, Object> response = new HashMap<>();
    if (authenticated) {
        response.put("success", true);
        response.put("message", "Login successful");
        return ResponseEntity.ok(response); // Return 200 OK for successful authentication
    } else {
        response.put("success", false);
        response.put("message", "Invalid email or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // Return 401 Unauthorized for failed authentication
    }
}

@PostMapping("/updatePassword")
public ResponseEntity<Map<String, String>> updatePassword(@RequestBody Map<String, String> payload) {
    String email = payload.get("email");
    String otp = payload.get("otp"); // Get the OTP from the request body
    String newPassword = payload.get("newPassword");

    Map<String, String> response = new HashMap<>();
    boolean updated = sservice.updatePassword(email, otp, newPassword);

    if (updated) {
        response.put("message", "Password updated successfully");
        return ResponseEntity.ok(response);
    } else {
        response.put("message", "Invalid OTP or user not found");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

}
