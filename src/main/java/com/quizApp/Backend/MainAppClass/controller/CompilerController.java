package com.quizApp.Backend.MainAppClass.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizApp.Backend.MainAppClass.model.CodeRequest;
import com.quizApp.Backend.MainAppClass.model.TestCase;
import com.quizApp.Backend.MainAppClass.repository.TestCaseRepository;

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
            ProcessBuilder compileProcessBuilder = new ProcessBuilder("/bin/sh", "-c", command + " " + sourceFile.getPath() + " -o output");
            Process compileProcess = compileProcessBuilder.start();

            // Capture compilation output and errors
            String compileErrors = getProcessErrorOutput(compileProcess);

            if (compileProcess.waitFor() != 0 || !compileErrors.isEmpty()) {
                return ResponseEntity.badRequest().body(compileErrors);
            }

            // Run the compiled code
            ProcessBuilder runProcessBuilder = new ProcessBuilder("/bin/sh", "-c", "./output");
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
        File outputFile = new File("output.exe");

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
            ProcessBuilder compileProcessBuilder = new ProcessBuilder("cmd", "/c", command, sourceFile.getPath(), "-o", outputFile.getPath());
            Process compileProcess = compileProcessBuilder.start();
            compileProcess.waitFor();

            // Capture compilation errors
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
            StringBuilder errors = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errors.append(line).append("\n");
            }

            if (compileProcess.exitValue() != 0 || errors.length() > 0) {
                sourceFile.delete();
                if (outputFile.exists()) {
                    outputFile.delete();
                }
                return ResponseEntity.badRequest().body("Compilation errors:\n" + errors.toString());
            }

            // Retrieve test cases
            List<TestCase> testCases = testCaseRepository.findTestCasesByQuestionSetIdAndQuestionNo(request.getQuestionSetId(), request.getQuestionNo());

            StringBuilder resultBuilder = new StringBuilder();
            boolean allTestsPassed = true;

            for (TestCase testCase : testCases) {
                String input = testCase.getTestCaseInput();
                String expectedOutput = testCase.getTestCaseOutput();

                // Run the compiled code
                ProcessBuilder runProcessBuilder = new ProcessBuilder("cmd", "/c", outputFile.getPath());
                Process runProcess = runProcessBuilder.start();

                // Provide input to the process
                if (input != null && !input.isEmpty()) {
                    try (BufferedWriter processInputWriter = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
                        processInputWriter.write(input);
                        processInputWriter.newLine(); // Ensure newlines are handled properly for multiple inputs
                        processInputWriter.flush();
                    }
                }

                // Capture the output and errors
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

                // Normalize and trim both expected and actual outputs
                String normalizedActualOutput = output.toString()
                        .replaceAll("\\r", "") // Remove carriage returns
                        .replaceAll("\\s+", " ") // Collapse whitespace
                        .trim();
                String normalizedExpectedOutput = expectedOutput
                        .replaceAll("\\r", "")
                        .replaceAll("\\s+", " ")
                        .trim();

                // Check test case results
                if (runProcess.exitValue() != 0 || runErrors.length() > 0) {
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