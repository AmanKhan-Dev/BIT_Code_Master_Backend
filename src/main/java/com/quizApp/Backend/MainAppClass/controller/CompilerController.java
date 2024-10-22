package com.quizApp.Backend.MainAppClass.controller;

import org.springframework.web.bind.annotation.*;
import com.quizApp.Backend.MainAppClass.model.CodeRequest;
import com.quizApp.Backend.MainAppClass.model.TestCase;
import com.quizApp.Backend.MainAppClass.repository.TestCaseRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/api/compiler")
public class CompilerController {

    private static final String GCC_PATH = "/usr/bin/gcc"; // For C
    private static final String GPP_PATH = "/usr/bin/g++"; // For C++

    @Autowired
    private TestCaseRepository testCaseRepository;

    @PostMapping("/compile")
    public ResponseEntity<String> compileCode(@RequestBody CodeRequest codeRequest) {
        String sourceCode = codeRequest.getSourceCode();
        String language = codeRequest.getLanguage(); // "C" or "C++"
        String userInput = codeRequest.getUserInput(); // User input for the program

        try {
            // Create source file
            File sourceFile = new File("source." + (language.equals("C") ? "c" : "cpp"));
            try (FileWriter writer = new FileWriter(sourceFile)) {
                writer.write(sourceCode);
            }

            // Compile
            String command = language.equals("C") ? GCC_PATH : GPP_PATH;
            ProcessBuilder compileProcessBuilder = new ProcessBuilder("/bin/bash", "-c", command + " " + sourceFile.getPath() + " -o output");
            Process compileProcess = compileProcessBuilder.start();
            
            // Capture compilation output and errors
            String compileErrors = getProcessErrorOutput(compileProcess);
            
            if (compileProcess.waitFor() != 0 || !compileErrors.isEmpty()) {
                return ResponseEntity.badRequest().body(compileErrors);
            }

            // Run the compiled code
            ProcessBuilder runProcessBuilder = new ProcessBuilder("/bin/bash", "-c", "./output");
            Process runProcess = runProcessBuilder.start();
            
            // Provide input to the process
            if (userInput != null && !userInput.isEmpty()) {
                try (BufferedWriter processInputWriter = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
                    processInputWriter.write(userInput);
                    processInputWriter.flush();
                }
            }

            // Capture output and errors
            String output = getProcessOutput(runProcess);
            String runErrors = getProcessErrorOutput(runProcess);
            
            if (runProcess.waitFor() != 0 || !runErrors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(runErrors);
            }

            return ResponseEntity.ok(output);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }

    // Helper method to capture process output
    private String getProcessOutput(Process process) throws IOException {
        try (BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = outputReader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString();
        }
    }

    // Helper method to capture process error output
    private String getProcessErrorOutput(Process process) throws IOException {
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            StringBuilder errors = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errors.append(line).append("\n");
            }
            return errors.toString();
        }
    }

    @PostMapping("/compileTests")
public ResponseEntity<String> compileCodeTest(@RequestBody CodeRequest request) {
    File sourceFile = new File("source." + (request.getLanguage().equals("C") ? "c" : "cpp"));
    File outputFile = new File("output");

    try {
        // Create source file
        try (FileWriter writer = new FileWriter(sourceFile)) {
            writer.write(request.getSourceCode());
        }

        // Compile the source code
        if (outputFile.exists()) {
            outputFile.delete();
        }

        String command = request.getLanguage().equals("C") ? GCC_PATH : GPP_PATH;
        ProcessBuilder compileProcessBuilder = new ProcessBuilder("/bin/bash", "-c", command + " " + sourceFile.getPath() + " -o " + outputFile.getPath());
        Process compileProcess = compileProcessBuilder.start();
        
        // Wait for the compile process to finish
        compileProcess.waitFor();

        // Capture compilation errors
        String errors = getProcessErrorOutput(compileProcess);

        if (compileProcess.exitValue() != 0 || !errors.isEmpty()) {
            sourceFile.delete();
            if (outputFile.exists()) {
                outputFile.delete();
            }
            return ResponseEntity.badRequest().body("Compilation errors:\n" + errors);
        }

        // Set output file as executable
        outputFile.setExecutable(true);

        // Retrieve test cases
        List<TestCase> testCases = testCaseRepository.findTestCasesByQuestionSetIdAndQuestionNo(request.getQuestionSetId(), request.getQuestionNo());

        StringBuilder resultBuilder = new StringBuilder();
        boolean allTestsPassed = true;

        for (TestCase testCase : testCases) {
            String input = testCase.getTestCaseInput();
            String expectedOutput = testCase.getTestCaseOutput();

            // Run the compiled code
            ProcessBuilder runProcessBuilder = new ProcessBuilder("/bin/bash", "-c", "./output");
          //  runProcessBuilder.directory(new File("/app")); // Set the working directory
            Process runProcess = runProcessBuilder.start();

            // Provide input to the process
            if (input != null && !input.isEmpty()) {
                try (BufferedWriter processInputWriter = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
                    processInputWriter.write(input);
                    processInputWriter.newLine();
                    processInputWriter.flush();
                }
            }

            // Wait for the run process to finish
            runProcess.waitFor();

            // Capture the output and errors
            String output = getProcessOutput(runProcess);
            String runErrors = getProcessErrorOutput(runProcess);

            // Normalize and trim both expected and actual outputs
            String normalizedActualOutput = output.toString().replace("\r", "").trim();
            String normalizedExpectedOutput = expectedOutput.replace("\r", "").trim();

            // Check test case results
            if (runProcess.exitValue() != 0 || !runErrors.isEmpty()) {
                resultBuilder.append("Test case failed for input: ").append(input).append("\n");
                resultBuilder.append("Error: ").append(runErrors).append("\n");
                allTestsPassed = false;
            } else if (!normalizedActualOutput.equals(normalizedExpectedOutput)) {
                resultBuilder.append("Test case failed for input: ").append(input).append("\n");
                resultBuilder.append("Expected: ").append(normalizedExpectedOutput).append("\n");
                resultBuilder.append("Actual: ").append(normalizedActualOutput).append("\n");
                allTestsPassed = false;
            } else {
                resultBuilder.append("Test case passed for input: ").append(input).append("\n");
            }
        }

        // Clean up
        sourceFile.delete();
        if (outputFile.exists()) {
            outputFile.delete();
        }

        if (allTestsPassed) {
            return ResponseEntity.ok("All Hidden Test Cases Passed");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultBuilder.toString());
        }

    } catch (IOException | InterruptedException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
    }
}
}