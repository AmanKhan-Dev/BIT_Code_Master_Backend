package com.quizApp.Backend.MainAppClass.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "question_sets")
public class Question_Set {
    @NotNull(message = "Set Id cannot be null")
    @Column(name = "Question_Set_Id")
    @Id
    private String questionSetId; // Ensure this matches the repository method

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    @Column(name = "set_password")
    private String setPassword;

    @NotNull(message = "Set Name cannot be null")
    @Column(name = "Question_Set_Name")
    private String questionSetName;
}
