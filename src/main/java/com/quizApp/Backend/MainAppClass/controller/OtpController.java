package com.quizApp.Backend.MainAppClass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.quizApp.Backend.MainAppClass.Service.OtpService;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/send")
    public String sendOtp(@RequestParam String email) {
        // Generate OTP
        String otp = otpService.generateOtp();

        // Send OTP via email using Brevo SMTP
        otpService.sendOtp(email, otp);

        // Optionally, save the OTP in the session or database
        return "OTP sent to " + email;
    }

    // Add verification logic if needed
}
