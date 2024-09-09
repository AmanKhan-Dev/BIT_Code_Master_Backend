// package com.quizApp.Backend.MainAppClass.controller;

// import com.quizApp.Backend.MainAppClass.model.CodeRequest;
// import com.quizApp.Backend.MainAppClass.model.TestCase;
// import com.quizApp.Backend.MainAppClass.repository.TestCaseRepository;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.io.*;
// import java.util.List;

// @RestController
// @RequestMapping("/compile")
// public class Code_Tester {

//     private static final String GCC_PATH = "C:\\gcc\\bin\\gcc"; // For C
//     private static final String GPP_PATH = "C:\\gcc\\bin\\g++"; // For C++

//     @Autowired
//     private TestCaseRepository testCaseRepository;

//     @PostMapping("/compilehere")
//     public ResponseEntity<String> compileCode(@RequestBody CodeRequest codeRequest) {
//         String sourceCode = codeRequest.getSourceCode();
//         String language = codeRequest.getLanguage(); // "C" or "C++"
//         String questionSetId = codeRequest.getQuestionSetId();
//         int questionNo = codeRequest.getQuestionNo();

//         try {
//             // Create source file
//             File sourceFile = new File("source." + (language.equals("C") ? "c" : "cpp"));
//             try (FileWriter writer = new FileWriter(sourceFile)) {
//                 writer.write(sourceCode);
//             }

//             // Compile the source code
//             File outputFile = new File("output.exe");
//             if (outputFile.exists()) {
//                 outputFile.delete();
//             }

//             String command = language.equals("C") ? GCC_PATH : GPP_PATH;
//             ProcessBuilder compileProcessBuilder = new ProcessBuilder("cmd", "/c", command, sourceFile.getPath(), "-o", "output.exe");
//             Process compileProcess = compileProcessBuilder.start();
//             compileProcess.waitFor();

//             BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
//             StringBuilder errors = new StringBuilder();
//             String line;
//             while ((line = errorReader.readLine()) != null) {
//                 errors.append(line).append("\n");
//             }

//             if (compileProcess.exitValue() != 0 || errors.length() > 0) {
//                 return ResponseEntity.badRequest().body(errors.toString());
//             }

//             // // Retrieve test cases for the given questionSetId and questionNo
//             // List<TestCase> testCases = testCaseRepository.findTestCasesByQuestionSetIdAndQuestionNo(questionSetId, questionNo);

//             // StringBuilder resultBuilder = new StringBuilder();
//             // for (TestCase testCase : testCases) {
//             //     String input = testCase.getTestCaseInput();
//             //     String expectedOutput = testCase.getTestCaseOutput();

//                 ProcessBuilder runProcessBuilder = new ProcessBuilder("cmd", "/c", "output.exe");
//                 Process runProcess = runProcessBuilder.start();

//                 // Provide input to the process
//                 try (BufferedWriter processInputWriter = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
//                     if (input != null && !input.isEmpty()) {
//                         processInputWriter.write(input);
//                         processInputWriter.flush();
//                     }
//                 }

//                 // Capture the output of the process
//                 BufferedReader outputReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
//                 StringBuilder output = new StringBuilder();
//                 while ((line = outputReader.readLine()) != null) {
//                     output.append(line).append("\n");
//                 }

//                 BufferedReader runErrorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
//                 StringBuilder runErrors = new StringBuilder();
//                 while ((line = runErrorReader.readLine()) != null) {
//                     runErrors.append(line).append("\n");
//                 }

//                 if (runProcess.exitValue() != 0 || runErrors.length() > 0) {
//                     resultBuilder.append("Test case failed for input: ").append(input).append("\n");
//                     resultBuilder.append("Error: ").append(runErrors).append("\n");
//                 } else if (!output.toString().trim().equals(expectedOutput.trim())) {
//                     resultBuilder.append("Test case failed for input: ").append(input).append("\n");
//                     resultBuilder.append("Expected: ").append(expectedOutput).append("\n");
//                     resultBuilder.append("Actual: ").append(output.toString().trim()).append("\n");
//                 } else {
//                     resultBuilder.append("Test case passed for input: ").append(input).append("\n");
//                 }
//             }

//             return resultBuilder.length() > 0 ? ResponseEntity.ok(resultBuilder.toString()) : ResponseEntity.ok("All test cases passed!");
//         } catch (IOException | InterruptedException e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//         }
//     }
// }
