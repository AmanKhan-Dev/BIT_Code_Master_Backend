package com.quizApp.Backend.MainAppClass.Service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quizApp.Backend.MainAppClass.model.Result;
import com.quizApp.Backend.MainAppClass.repository.ResultRepository;

@Service
public class ResultService {

    
    @Autowired
    ResultRepository resultRepository;



    
    public List<Result> getResultByEmail(String email) {
        return resultRepository.findByEmail(email);
    }


public Result saveResult(Result result){

    resultRepository.save(result);
    return result;
}







}
