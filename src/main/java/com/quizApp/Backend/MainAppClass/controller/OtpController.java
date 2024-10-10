package com.quizApp.Backend.MainAppClass.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.quizApp.Backend.MainAppClass.Service.OtpService;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    private String currentOtp; // Store the current OTP temporarily

    @PostMapping("/send")
    public String sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email"); // Get the email from the request body
        String otp = otpService.generateOtp();
        currentOtp = otp; // Store OTP for verification
        otpService.sendOtp(email, otp);
        return "OTP sent to " + email;
    }

    @PostMapping("/verify")
    public String verifyOtp(@RequestBody Map<String, String> request) {
        String otp = request.get("otp"); // Get the OTP from the request body
        System.out.println("Verifying OTP: " + otp);
        System.out.println("Current OTP: " + currentOtp);
    
        if (currentOtp == null) {
            throw new RuntimeException("No OTP was generated. Please request an OTP first.");
        }
        if (otp.equals(currentOtp)) {
            return "OTP verified successfully!";
        } else {
            throw new RuntimeException("Invalid OTP.");
        }
    }
    

}
