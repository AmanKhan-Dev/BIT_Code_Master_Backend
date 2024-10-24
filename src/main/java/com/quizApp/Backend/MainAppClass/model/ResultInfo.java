package com.quizApp.Backend.MainAppClass.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResultInfo {
    private String studentName;
    private String prn;
    private String email;
}
