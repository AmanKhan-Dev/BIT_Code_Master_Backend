package com.quizApp.Backend.MainAppClass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quizApp.Backend.MainAppClass.model.Code_Saver;
import com.quizApp.Backend.MainAppClass.model.Code_Saver_Id;

@Repository
public interface Code_Saver_repo extends JpaRepository<Code_Saver, Code_Saver_Id> {
}
