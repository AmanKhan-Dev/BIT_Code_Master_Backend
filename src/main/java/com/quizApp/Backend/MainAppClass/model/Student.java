package com.quizApp.Backend.MainAppClass.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_details")
public class Student {

    @NotBlank(message = "Full Name is required")
    @Column(name = "Full_Name")
    private String full_name;

    @Email(message = "Email should be valid")
    @Id
    @Column(name = "Email")
    private String email;

    @Positive(message = "PRN should be a positive number")
    @Column(name = "PRN")
    private long prn_no;

    @NotBlank(message = "Roll Number is required")
    @Column(name = "Roll_No")
    private String roll_no;
}