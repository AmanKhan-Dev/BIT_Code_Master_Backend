package com.quizApp.Backend.MainAppClass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizApp.Backend.MainAppClass.Service.Code_Saver_Service;
import com.quizApp.Backend.MainAppClass.model.Code_Saver;

@RestController 
@RequestMapping("/code") 
public class Code_Saver_Controller {

    @Autowired 
    private Code_Saver_Service code_Saver_Service;

    @PostMapping("/save")
    public ResponseEntity<String> saveCode(@RequestBody Code_Saver code_Saver) {
        return code_Saver_Service.saveCode(code_Saver);
    }
}
