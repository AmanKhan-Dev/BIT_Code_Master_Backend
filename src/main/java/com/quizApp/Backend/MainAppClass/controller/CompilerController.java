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

    private static final String GCC_PATH = "C:\\gcc\\bin\\gcc"; // For C
    private static final String GPP_PATH = "C:\\gcc\\bin\\g++"; // For C++

    @Autowired
    private TestCaseRepository testCaseRepository;
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

            // Check test case results
            if (runProcess.exitValue() != 0 || runErrors.length() > 0) {
                resultBuilder.append("Test case failed for input: ").append(input).append("\n");
                resultBuilder.append("Error: ").append(runErrors).append("\n");
                allTestsPassed = false;
            } else if (!output.toString().trim().equals(expectedOutput.trim())) {
                resultBuilder.append("Test case failed for input: ").append(input).append("\n");
                resultBuilder.append("Expected: ").append(expectedOutput).append("\n");
                resultBuilder.append("Actual: ").append(output.toString().trim()).append("\n");
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

        // Send appropriate response based on test results
        if (allTestsPassed) {
            return ResponseEntity.ok("All test cases passed!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultBuilder.toString());
        }

    } catch (IOException | InterruptedException e) {
        // Handle exceptions and return a proper response
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
    }
}

}




