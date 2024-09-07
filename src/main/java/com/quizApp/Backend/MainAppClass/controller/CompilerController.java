package com.quizApp.Backend.MainAppClass.controller;

import org.springframework.web.bind.annotation.*;
import com.quizApp.Backend.MainAppClass.model.CodeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.*;

@RestController
@RequestMapping("/api/compiler")
public class CompilerController {

    private static final String GCC_PATH = "C:\\gcc\\bin\\gcc"; // For C
    private static final String GPP_PATH = "C:\\gcc\\bin\\g++"; // For C++

    @PostMapping("/compile")
public ResponseEntity<String> compileCode(@RequestBody CodeRequest codeRequest) {
    String sourceCode = codeRequest.getSourceCode();
    String language = codeRequest.getLanguage(); // "C" or "C++"
    String userInput = codeRequest.getUserInput(); // User input for the program

    try {
        File sourceFile = new File("source." + (language.equals("C") ? "c" : "cpp"));
        try (FileWriter writer = new FileWriter(sourceFile)) {
            writer.write(sourceCode);
        }

        File outputFile = new File("output.exe");
        if (outputFile.exists()) {
            outputFile.delete();
        }

        String command = language.equals("C") ? GCC_PATH : GPP_PATH;
        ProcessBuilder compileProcessBuilder = new ProcessBuilder("cmd", "/c", command, sourceFile.getPath(), "-o", "output.exe");
        Process compileProcess = compileProcessBuilder.start();
        compileProcess.waitFor();

        BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
        StringBuilder errors = new StringBuilder();
        String line;
        while ((line = errorReader.readLine()) != null) {
            errors.append(line).append("\n");
        }

        if (compileProcess.exitValue() != 0 || errors.length() > 0) {
            return ResponseEntity.badRequest().body(errors.toString());
        }

        ProcessBuilder runProcessBuilder = new ProcessBuilder("cmd", "/c", "output.exe");
        Process runProcess = runProcessBuilder.start();

        try (BufferedWriter processInputWriter = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
            if (userInput != null && !userInput.isEmpty()) {
                processInputWriter.write(userInput);
                processInputWriter.flush();
            }
        }

        BufferedReader outputReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
        StringBuilder output = new StringBuilder();
        while ((line = outputReader.readLine()) != null) {
            output.append(line).append("\n");
        }

        BufferedReader runErrorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
        StringBuilder runErrors = new StringBuilder();
        while ((line = runErrorReader.readLine()) != null) {
            runErrors.append(line).append("\n");
        }

        if (runProcess.exitValue() != 0 || runErrors.length() > 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(runErrors.toString());
        }

        return ResponseEntity.ok(output.toString());
    } catch (IOException | InterruptedException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}

}
