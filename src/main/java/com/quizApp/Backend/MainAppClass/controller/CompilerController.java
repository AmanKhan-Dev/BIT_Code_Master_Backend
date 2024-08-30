package com.quizApp.Backend.MainAppClass.controller;

import org.springframework.web.bind.annotation.*;
import com.quizApp.Backend.MainAppClass.model.CodeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api/compiler")
public class CompilerController {

    // Update these paths based on your GCC and G++ installation
    private static final String GCC_PATH = "C:\\gcc\\bin\\gcc"; // For C
    private static final String GPP_PATH = "C:\\gcc\\bin\\g++"; // For C++

    @PostMapping("/compile")
    public ResponseEntity<String> compileCode(@RequestBody CodeRequest codeRequest) {
        String sourceCode = codeRequest.getSourceCode();
        String language = codeRequest.getLanguage(); // "C" or "C++"

        try {
            // Write the source code to a file
            File file = new File("source." + (language.equals("C") ? "c" : "cpp"));
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(sourceCode);
            }

            // Compile the code
            String command = language.equals("C") ? GCC_PATH : GPP_PATH;
            ProcessBuilder processBuilder = new ProcessBuilder(command, file.getPath(), "-o", "output");
            Process process = processBuilder.start();
            process.waitFor();

            // Check for compilation errors
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder errors = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errors.append(line).append("\n");
            }

            if (errors.length() > 0) {
                return ResponseEntity.badRequest().body(errors.toString());
            }

            return ResponseEntity.ok("Compilation successful!");
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
