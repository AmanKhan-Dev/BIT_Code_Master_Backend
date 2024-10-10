package com.quizApp.Backend.MainAppClass.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    private String currentOtp; // Store the current OTP temporarily

    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        currentOtp = String.valueOf(otp); // Store the generated OTP
        return currentOtp;
    }

    public String getCurrentOtp() {
        return currentOtp;
    }

    public void clearCurrentOtp() {
        currentOtp = null; // Clear the OTP
    }

    // Method to send OTP via Brevo SMTP
    public void sendOtp(String email) {
        String otp = generateOtp(); // Generate OTP
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("uchihaitachi39990@gmail.com");
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);

        try {
            mailSender.send(message);
            System.out.println("OTP sent successfully to " + email);
        } catch (MailException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send OTP email.");
        }
    }
}
