package com.quizApp.Backend.MainAppClass.controller;

import org.springframework.web.bind.annotation.*;
import com.quizApp.Backend.MainAppClass.model.CodeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.*;

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
        String userInput = codeRequest.getUserInput(); // User input for the program

        try {
            // Write the source code to a file
            File sourceFile = new File("source." + (language.equals("C") ? "c" : "cpp"));
            try (FileWriter writer = new FileWriter(sourceFile)) {
                writer.write(sourceCode);
            }

            // Clean up any previous output file
            File outputFile = new File("output.exe");
            if (outputFile.exists()) {
                outputFile.delete();
            }

            // Compile the code
            String command = language.equals("C") ? GCC_PATH : GPP_PATH;
            ProcessBuilder compileProcessBuilder = new ProcessBuilder("cmd", "/c", command, sourceFile.getPath(), "-o", "output.exe");
            Process compileProcess = compileProcessBuilder.start();
            compileProcess.waitFor();

            // Check for compilation errors
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
            StringBuilder errors = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errors.append(line).append("\n");
            }

            if (errors.length() > 0) {
                return ResponseEntity.badRequest().body(errors.toString());
            }

            // Run the compiled code and capture the output
            ProcessBuilder runProcessBuilder = new ProcessBuilder("cmd", "/c", "output.exe");
            Process runProcess = runProcessBuilder.start();

            // Provide user input to the process
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

            // Check for runtime errors
            BufferedReader runErrorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
            StringBuilder runErrors = new StringBuilder();
            while ((line = runErrorReader.readLine()) != null) {
                runErrors.append(line).append("\n");
            }

            if (runErrors.length() > 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(runErrors.toString());
            }

            return ResponseEntity.ok(output.toString());
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
