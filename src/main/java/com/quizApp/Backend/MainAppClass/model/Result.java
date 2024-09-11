package com.quizApp.Backend.MainAppClass.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@IdClass(ResultId.class)
@Table(name = "student_results")
public class Result {

    @Id
    @Column(name = "question_set_id")
    private String questionSetId;

   @Id
    @Column(name = "question_no")
    private int questionNo;
    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "prn")
    private String prn;

    @Column(name = "student_roll_no")
    private String studentRollNo;

    @Column(name = "solved_counter")
    private int solvingStatus;

}