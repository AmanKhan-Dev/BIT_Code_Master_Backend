package com.quizApp.Backend.MainAppClass.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@IdClass(TestcaseId.class)
public class TestCase {


  
    @Column(name = "Question_Set_Id")
    @Id
    private String questionSetId;


    @Column(name = "Question_no")
    @Id
    private int questionNo;
    

    @Id
    @Column(name = "test_case_input", columnDefinition = "TEXT")
    private String testCaseInput;


    @Column(name = "test_case_output",columnDefinition = "TEXT")
    private String testCaseOutput;
    
}
