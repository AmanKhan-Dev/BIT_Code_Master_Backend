package com.quizApp.Backend.MainAppClass.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quizApp.Backend.MainAppClass.model.Code_Saver;
import com.quizApp.Backend.MainAppClass.model.Code_Saver_Id;
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


    public ResponseEntity<String> getCode(String student_email, String question_set_id, int question_no) {
        Code_Saver_Id codeSaverId = new Code_Saver_Id(student_email, question_set_id, question_no);
        Optional<Code_Saver> codeSaver = code_Saver_repo.findById(codeSaverId);
        if (codeSaver.isPresent()) {
            return new ResponseEntity<>(codeSaver.get().getCode(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Code not found", HttpStatus.NOT_FOUND);
        }
    }
}
