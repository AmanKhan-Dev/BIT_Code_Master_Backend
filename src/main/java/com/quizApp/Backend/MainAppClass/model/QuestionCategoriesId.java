package com.quizApp.Backend.MainAppClass.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class QuestionCategoriesId implements Serializable {
      @Column(name = "question_set_id")
    private String question_set_id;

    @Column(name = "available_category")
    private String available_category;

    // Getters, Setters, hashCode, and equals methods
    public String getQuestion_set_id() {
        return question_set_id;
    }

    public void setQuestion_set_id(String question_set_id) {
        this.question_set_id = question_set_id;
    }

    public String getAvailable_category() {
        return available_category;
    }

    public void setAvailable_category(String available_category) {
        this.available_category = available_category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(question_set_id, available_category);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuestionCategoriesId)) return false;
        QuestionCategoriesId that = (QuestionCategoriesId) obj;
        return Objects.equals(question_set_id, that.question_set_id) &&
               Objects.equals(available_category, that.available_category);
    }
}
