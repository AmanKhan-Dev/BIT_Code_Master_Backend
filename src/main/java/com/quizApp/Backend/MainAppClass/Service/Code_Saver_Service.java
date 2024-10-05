package com.quizApp.Backend.MainAppClass.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quizApp.Backend.MainAppClass.model.Code_Saver;
import com.quizApp.Backend.MainAppClass.repository.Code_Saver_repo;

@Service
public class Code_Saver_Service {

    @Autowired
    private Code_Saver_repo code_Saver_repo;

    public ResponseEntity<String> saveCode(Code_Saver code_Saver) {
        try {
            code_Saver_repo.save(code_Saver);
            return new ResponseEntity<>("Code Saved Successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving code: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
