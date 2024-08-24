package com.quizApp.Backend.MainAppClass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quizApp.Backend.MainAppClass.model.Admin;
@Repository
public interface Admin_Repository extends JpaRepository <Admin,String> {
boolean existsByEmail(String email);
    

    
}
