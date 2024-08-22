package com.quizApp.Backend.MainAppClass.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quizApp.Backend.MainAppClass.model.Student;
import com.quizApp.Backend.MainAppClass.repository.Student_Repository;

@Service
public class Student_Service {
    @Autowired
    Student_Repository srepository;

    public Iterable <Student> getAllStudents(){
       return  srepository.findAll();
    }

    public void saveStudent(Student student){
         srepository.save(student);
    }
    public boolean existsByEmail(String email) {
        return srepository.existsByEmail(email);
    }
    


}
