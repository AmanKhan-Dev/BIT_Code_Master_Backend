package com.quizApp.Backend.MainAppClass.model;

import java.io.Serializable;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestcaseId implements Serializable {
    private String questionSetId;
    private int questionNo;
    private int tcid;
}
