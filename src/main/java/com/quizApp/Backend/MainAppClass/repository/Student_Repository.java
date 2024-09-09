package com.quizApp.Backend.MainAppClass.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quizApp.Backend.MainAppClass.model.Student;
@Repository
public interface Student_Repository extends JpaRepository<Student,String> {
    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);
    
}
