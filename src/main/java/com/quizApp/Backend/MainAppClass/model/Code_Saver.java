package com.quizApp.Backend.MainAppClass.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "code_saver")
public class Code_Saver {

    @EmbeddedId
    private Code_Saver_Id codeSaverId;

    @Column(name = "code", columnDefinition = "TEXT")
    private String code;
}
