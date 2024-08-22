package com.quizApp.Backend.MainAppClass.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizApp.Backend.MainAppClass.Service.Student_Service;
import com.quizApp.Backend.MainAppClass.model.Student;
@RequestMapping("student")
@RestController
public class Student_Controller {
    @Autowired
    Student_Service sservice;

    @GetMapping("/all")
    public Iterable<Student> getAllStudents(){
             return sservice.getAllStudents();
    }
    

    @PostMapping("/saveStudent")
    public void saveStudent( @RequestBody Student student){
        sservice.saveStudent(student);
    }
}
