package com.quizApp.Backend.MainAppClass.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "test_cases")
public class TestCase {


    @NotBlank(message = "Question Set ID must not be blank")
    @Column(name = "Question_Set_Id")
    @Id
    private String questionSetId;
    @NotBlank(message = "Question Set ID must not be blank")
    @Column(name = "Question_no")
    private int questionNo;


    @Column(name = "test_case_input")
    private String testCaseInput;


    @Column(name = "test_case_output")
    private String testCaseOutput;
    
}
