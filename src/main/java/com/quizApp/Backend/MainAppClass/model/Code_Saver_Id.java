package com.quizApp.Backend.MainAppClass.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Code_Saver_Id implements Serializable {
    private String student_email;
    private String question_set_id;
    private int question_no;
}
