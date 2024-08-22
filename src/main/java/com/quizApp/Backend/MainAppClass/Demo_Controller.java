package com.quizApp.Backend.MainAppClass;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Demo_Controller {
    @GetMapping("/see")
    public String seedemo(){
        return "hello new project";
    }
}
