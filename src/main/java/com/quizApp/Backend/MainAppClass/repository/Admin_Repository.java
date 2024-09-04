package com.quizApp.Backend.MainAppClass.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quizApp.Backend.MainAppClass.model.Admin;

@Repository
public interface Admin_Repository extends JpaRepository <Admin,String> {

   Optional<Admin> findByEmail(String email);
   boolean existsByEmail(String email);
    

    
}
