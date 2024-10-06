package com.quizApp.Backend.MainAppClass.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class QuestionKey implements Serializable {
    
    private String questionSetId;
    private int questionNo;

   
    // Getters and Setters
    public String getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(String questionSetId) {
        this.questionSetId = questionSetId;
    }

    public int getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(int questionNo) {
        this.questionNo = questionNo;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionKey)) return false;
        QuestionKey that = (QuestionKey) o;
        return questionNo == that.questionNo && Objects.equals(questionSetId, that.questionSetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionSetId, questionNo);
    }
}
