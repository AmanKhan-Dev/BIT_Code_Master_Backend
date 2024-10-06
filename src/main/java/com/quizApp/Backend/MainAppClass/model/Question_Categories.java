package com.quizApp.Backend.MainAppClass.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "question_set_categories")
public class Question_Categories {

    @EmbeddedId
    private QuestionCategoriesId id;

    // Other fields can be added here if necessary
}
