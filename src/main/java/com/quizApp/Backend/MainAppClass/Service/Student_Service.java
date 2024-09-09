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
    


}
