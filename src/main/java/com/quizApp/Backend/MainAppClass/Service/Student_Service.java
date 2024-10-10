package com.quizApp.Backend.MainAppClass.Service;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quizApp.Backend.MainAppClass.model.Student;
import com.quizApp.Backend.MainAppClass.repository.Student_Repository;

@Service
public class Student_Service {
    @Autowired
    Student_Repository srepository;

    @Autowired
    OtpService otpService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Iterable <Student> getAllStudents(){
       return  srepository.findAll();
    }

    public void saveStudent(Student student){

        String encodedPassword = passwordEncoder.encode(student.getPassword());
        student.setPassword(encodedPassword);
         srepository.save(student);
    }
    public boolean existsByEmail(String email) {
        return srepository.existsByEmail(email);
    }

    public boolean authenticate(String email, String rawPassword) {
        return srepository.findByEmail(email)
            .map(student -> passwordEncoder.matches(rawPassword, student.getPassword()))
            .orElse(false);
    }
     // New method to find a student by email
     public Optional<Student> findStudentByEmail(String email) {
        return srepository.findByEmail(email);
    }
    public boolean updatePassword(String email, String otp, String newPassword) {
        Optional<Student> studentOptional = srepository.findByEmail(email);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
    
            // Verify the OTP
            if (otp.equals(otpService.getCurrentOtp())) { // Check if the provided OTP matches the stored OTP
                String encodedNewPassword = passwordEncoder.encode(newPassword);
                student.setPassword(encodedNewPassword);
                srepository.save(student);
                otpService.clearCurrentOtp(); // Clear the OTP after successful use
                return true; // Password updated successfully
            }
        }
        return false; // Password update failed
    }
    
    
}
