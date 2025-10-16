package com.quizApp.Backend.MainAppClass.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionSetId;
    private Integer questionNo;

    @Column(length = 5000)
    private String sourceCode;

    @Column(length = 5000)
    private String feedback;
}
