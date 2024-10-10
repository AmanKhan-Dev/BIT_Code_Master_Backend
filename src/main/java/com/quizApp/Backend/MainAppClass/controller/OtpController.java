package com.quizApp.Backend.MainAppClass.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.quizApp.Backend.MainAppClass.Service.OtpService;
import com.quizApp.Backend.MainAppClass.Service.Student_Service;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private Student_Service studentService; // Make sure to inject your StudentService

    @PostMapping("/send")
    public String sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email"); // Get the email from the request body
        otpService.sendOtp(email);
        return "OTP sent to " + email;
    }

    @PostMapping("/verify")
    public String verifyOtp(@RequestBody Map<String, String> request) {
        String otp = request.get("otp"); // Get the OTP from the request body
        System.out.println("Verifying OTP: " + otp);
        String currentOtp = otpService.getCurrentOtp();

        if (currentOtp == null) {
            throw new RuntimeException("No OTP was generated. Please request an OTP first.");
        }
        if (otp.equals(currentOtp)) {
            return "OTP verified successfully!";
        } else {
            throw new RuntimeException("Invalid OTP.");
        }
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String otp = payload.get("otp"); // Get the OTP from the request body
        String newPassword = payload.get("newPassword");

        Map<String, String> response = new HashMap<>();
        boolean updated = studentService.updatePassword(email, otp, newPassword);

        if (updated) {
            response.put("message", "Password updated successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid OTP or user not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
