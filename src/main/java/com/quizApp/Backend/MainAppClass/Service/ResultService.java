package com.quizApp.Backend.MainAppClass.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quizApp.Backend.MainAppClass.model.Result;
import com.quizApp.Backend.MainAppClass.model.ResultInfo;
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



public List<ResultInfo> getResultInfoByQuestionSetIdAndQuestionNo(String questionSetId, int questionNo) {
        List<Result> results = resultRepository.findResultsByQuestionSetIdAndQuestionNo(questionSetId, questionNo);
        return results.stream()
                .map(result -> new ResultInfo(result.getStudentName(), result.getPrn(), result.getEmail()))
                .collect(Collectors.toList());
    }




}
