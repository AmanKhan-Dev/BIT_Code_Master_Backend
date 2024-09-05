package com.quizApp.Backend.MainAppClass.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coding_questions")

public class Question_Adder {
    @Id
    @Column(name = "Question_Set_Id")
    @NotBlank(message = "Question Set ID must not be blank")
    private String questionSetId;

    @Column(name = "Question_no")
    private int questionNo;

    @Column(name = "question", columnDefinition = "TEXT")
    @NotBlank(message = "Question cannot be blank")
    private String question;

    @Column(name = "question_description", columnDefinition = "TEXT")
    private String question_description;

    public void setSerialNumber(int i) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSerialNumber'");
    }
}
