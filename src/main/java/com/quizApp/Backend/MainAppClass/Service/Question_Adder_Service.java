package com.quizApp.Backend.MainAppClass.Service;

import com.quizApp.Backend.MainAppClass.repository.Question_Adder_Repo;
import com.quizApp.Backend.MainAppClass.model.Question_Adder;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Question_Adder_Service {

    @Autowired
    private Question_Adder_Repo repository;

    public void insertIfNotExists(Question_Adder questionAdder) {
        repository.insertIfNotExists(questionAdder.getQuestionSetId(), questionAdder.getQuestionNo(), questionAdder.getQuestion(),questionAdder.getQuestion_description(),questionAdder.getTest_case_input(),questionAdder.getTest_case_output());
    }

    public void updateQuestion(Question_Adder myAdder) {
        repository.updateQuestion(myAdder.getQuestionSetId(),myAdder.getQuestionNo(),myAdder.getQuestion(),myAdder.getQuestion_description(),myAdder.getTest_case_input(),myAdder.getTest_case_output());
    }



    public String getQuestionBySetIdAndNo(String questionSetId, int questionNo) {
        
       Question_Adder questionEntity = repository.findByQuestionSetIdAndQuestionNo(questionSetId, questionNo);
        

        return (questionEntity != null) ? questionEntity.getQuestion() : null;
    }


    public List<Question_Adder> getAllQuestions(){
       return repository.findAll();
    }

    public List<String> getQuestionsBySetId(String questionSetId) {
        return repository.findQuestionsBySetId(questionSetId);
    }
   

}
