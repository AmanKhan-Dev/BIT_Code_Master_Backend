package com.quizApp.Backend.MainAppClass.controller;

import com.quizApp.Backend.MainAppClass.model.CodeRequest;
import com.quizApp.Backend.MainAppClass.model.TestCase;
import com.quizApp.Backend.MainAppClass.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        String questionSetId = codeRequest.getQuestionSetId();
        int questionNo = codeRequest.getQuestionNo();

        File sourceFile = new File("source." + (language.equals("C") ? "c" : "cpp"));
        File outputFile = new File("output.exe");

        try {
            // Write source code to file
            try (FileWriter writer = new FileWriter(sourceFile)) {
                writer.write(sourceCode);
            }

            // Compile the source code
            if (outputFile.exists()) {
                outputFile.delete();
            }

            String compileCommand = language.equals("C") ? GCC_PATH : GPP_PATH;
            ProcessBuilder compileProcessBuilder = new ProcessBuilder("cmd", "/c", compileCommand, sourceFile.getPath(), "-o", outputFile.getPath());
            Process compileProcess = compileProcessBuilder.start();
            compileProcess.waitFor();

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
            StringBuilder compileErrors = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                compileErrors.append(line).append("\n");
            }

            if (compileProcess.exitValue() != 0 || compileErrors.length() > 0) {
                return ResponseEntity.badRequest().body(compileErrors.toString());
            }

            // Retrieve test cases
            List<TestCase> testCases = testCaseRepository.findTestCasesByQuestionSetIdAndQuestionNo(questionSetId, questionNo);
            if (testCases.isEmpty()) {
                return ResponseEntity.badRequest().body("No test cases found for the provided question set ID and question number.");
            }

            // Execute compiled code with each test case
            StringBuilder resultBuilder = new StringBuilder();
            boolean allPassed = true;

            for (TestCase testCase : testCases) {
                String input = testCase.getTestCaseInput();
                String expectedOutput = testCase.getTestCaseOutput();

                // Clear previous output file
                if (outputFile.exists()) {
                    outputFile.delete();
                }

                ProcessBuilder runProcessBuilder = new ProcessBuilder("cmd", "/c", outputFile.getPath());
                Process runProcess = runProcessBuilder.start();

                // Provide input to the process
                try (BufferedWriter processInputWriter = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
                    if (input != null && !input.isEmpty()) {
                        processInputWriter.write(input);
                        processInputWriter.flush();
                    }
                }

                // Capture the output of the process
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
                    resultBuilder.append("Test case failed for input: ").append(input).append("\n");
                    resultBuilder.append("Error: ").append(runErrors).append("\n");
                    allPassed = false;
                } else if (!output.toString().trim().equals(expectedOutput.trim())) {
                    resultBuilder.append("Test case failed for input: ").append(input).append("\n");
                    resultBuilder.append("Expected: ").append(expectedOutput).append("\n");
                    resultBuilder.append("Actual: ").append(output.toString().trim()).append("\n");
                    allPassed = false;
                } else {
                    resultBuilder.append("Test case passed for input: ").append(input).append("\n");
                }
            }

            // Return a summary of results
            if (allPassed) {
                return ResponseEntity.ok("All test cases passed!");
            } else {
                return ResponseEntity.ok(resultBuilder.toString());
            }

        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } finally {
            // Clean up files
            if (sourceFile.exists()) {
                sourceFile.delete();
            }
            if (outputFile.exists()) {
                outputFile.delete();
            }
        }
    }
}
