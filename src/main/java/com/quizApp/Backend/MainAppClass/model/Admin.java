package com.quizApp.Backend.MainAppClass.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin_details")
public class Admin {

    @NotBlank(message = "Full Name is required")
    @Column(name = "admin_full_Name")
    private String admin_full_name;

    @Email(message = "Email should be valid")
    @Id
    @Column(name = "admin_email")
    private String email;

    @Positive(message = "Admin ID should be a positive number")
    @Column(name = "admin_id")
    private long admin_id;

     @Size(min = 8,message = "Password Must Contain At Least 8 Characters")
    @NotBlank(message = "Password cannot ne blank")
    @Column(name = "admin_password")
    private String admin_password;
}
