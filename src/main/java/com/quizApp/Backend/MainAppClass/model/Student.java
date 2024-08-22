package com.quizApp.Backend.MainAppClass.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_details")
public class Student {
    @Column(name = "Full_Name")
    private String full_name;
    @Id
    @Column(name = "Email")
    private String email;

    @Column(name = "PRN")
    private long prn_no;

    @Column (name = "Roll_No")
    private String roll_no;
}
