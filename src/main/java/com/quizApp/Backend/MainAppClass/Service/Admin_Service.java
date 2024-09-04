package com.quizApp.Backend.MainAppClass.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quizApp.Backend.MainAppClass.model.Admin;
import com.quizApp.Backend.MainAppClass.repository.Admin_Repository;


@Service
public class Admin_Service {

    @Autowired
    Admin_Repository rrepository;
    @Autowired
    PasswordEncoder pencoder;
    
    public void saveAdmin(Admin admin){
    String encodedpassword = pencoder.encode(admin.getAdmin_password());
  admin.setAdmin_password(encodedpassword);
      rrepository.save(admin);
    }


    public boolean existsByEmail(String email) {
        return rrepository.existsByEmail(email);
    }


    public boolean authenticateAdmin(String email, String uncodedPassword){
      return rrepository.findByEmail(email)
      .map(admin->pencoder.matches(uncodedPassword, admin.getAdmin_password()))
      .orElse(false);
    }
  }
    



