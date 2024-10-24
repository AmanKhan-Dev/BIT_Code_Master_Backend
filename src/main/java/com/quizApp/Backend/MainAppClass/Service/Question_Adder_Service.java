package com.quizApp.Backend.MainAppClass.Service;

import com.quizApp.Backend.MainAppClass.repository.Question_Adder_Repo;
import com.quizApp.Backend.MainAppClass.model.Question_Adder;
import com.quizApp.Backend.MainAppClass.model.QuestionKey;
import com.quizApp.Backend.MainAppClass.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Question_Adder_Service {

    @Autowired
    private Question_Adder_Repo repository;




    public List<Map<String, Object>>findQuestionCount(String questionSetId) {
        List<Object[]> results = repository.findQuestionCount(questionSetId);
        List<Map<String, Object>> questions = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> questionMap = new HashMap<>();
            questionMap.put("questionNo", result[0]);
          
            questions.add(questionMap);
        }

        return questions;
    }













    public void insertIfNotExists(Question_Adder questionAdder) {
        repository.insertIfNotExists(
            questionAdder.getId().getQuestionSetId(), 
            questionAdder.getId().getQuestionNo(), 
            questionAdder.getQuestion(),
            questionAdder.getQuestion_description(),
            questionAdder.getTest_case_input(),
            questionAdder.getTest_case_output(),
            questionAdder.getQuestion_category() // Include question_category
        );
    }

    public void updateQuestion(Question_Adder myAdder) {
        repository.updateQuestion(
            myAdder.getId().getQuestionSetId(),
            myAdder.getId().getQuestionNo(),
            myAdder.getQuestion(),
            myAdder.getQuestion_description(),
            myAdder.getTest_case_input(),
            myAdder.getTest_case_output(),
            myAdder.getQuestion_category() // Include question_category
        );
    }

    public Optional getQuestionBySetIdAndNo(String questionSetId, int questionNo) {
        return repository.findById(new QuestionKey(questionSetId, questionNo));
    }

    public List<Question_Adder> getAllQuestions() {
        return repository.findAll();
    }

    public List<Map<String, Object>> getQuestionsBySetId(String questionSetId) {
        List<Object[]> results = repository.findQuestionsBySetId(questionSetId);
        List<Map<String, Object>> questions = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> questionMap = new HashMap<>();
            questionMap.put("questionNo", result[0]);
            questionMap.put("question", result[1]);
            questionMap.put("questionCategory", result[2]);
            questions.add(questionMap);
        }

        return questions;
    }

    public Optional<Student> findStudentByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findStudentByEmail'");
    }




  



}
