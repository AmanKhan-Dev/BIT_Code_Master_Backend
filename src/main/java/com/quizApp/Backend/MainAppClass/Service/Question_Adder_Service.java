package com.quizApp.Backend.MainAppClass.Service;

import com.quizApp.Backend.MainAppClass.repository.Question_Adder_Repo;
import com.quizApp.Backend.MainAppClass.model.Question_Adder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Question_Adder_Service {

    @Autowired
    private Question_Adder_Repo repository;

    public void insertIfNotExists(Question_Adder questionAdder) {
        repository.insertIfNotExists(questionAdder.getQuestionSetId(), questionAdder.getQuestionNo(), questionAdder.getQuestion());
    }

    public void updateQuestion(Question_Adder myAdder) {
        repository.updateQuestion(myAdder.getQuestionSetId(),myAdder.getQuestionNo(),myAdder.getQuestion());
    }
}
