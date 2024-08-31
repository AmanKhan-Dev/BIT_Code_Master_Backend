package com.quizApp.Backend.MainAppClass.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quizApp.Backend.MainAppClass.model.Question_Set;
import com.quizApp.Backend.MainAppClass.repository.SetRepository;

@Service
public class Question_Set_Service {

    @Autowired
    SetRepository setRepository;

    @Autowired
    PasswordEncoder encoder;

    public void saveSet(Question_Set set){

        String encodedPassword = encoder.encode(set.getSetPassword());
        set.setSetPassword(encodedPassword);
        setRepository.save(set);

    }

    public Iterable <Question_Set> getAllQuestionSets(){
        return setRepository.findAll();
    }


    
}
