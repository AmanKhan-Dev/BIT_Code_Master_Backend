package com.quizApp.Backend.MainAppClass.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResultId implements Serializable{
    private String email;
    private int questionNo;
    private String questionSetId;

    
}
