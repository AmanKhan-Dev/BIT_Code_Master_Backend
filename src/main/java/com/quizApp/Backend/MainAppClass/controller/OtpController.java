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
        
        String otp = otpService.generateOtp();

        // Send OTP via email using Brevo SMTP
        otpService.sendOtp(email, otp);

        
        return "OTP sent to " + email;
    }

   
}
