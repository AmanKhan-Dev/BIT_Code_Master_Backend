package com.quizApp.Backend.MainAppClass.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quizApp.Backend.MainAppClass.model.Admin;
import com.quizApp.Backend.MainAppClass.repository.Admin_Repository;


@Service
public class Admin_Service {

    @Autowired
    Admin_Repository rrepository;
    public void saveAdmin(Admin admin){

      
      rrepository.save(admin);
    }
    public boolean existsByEmail(String email) {
        return rrepository.existsByEmail(email);
    }


}
