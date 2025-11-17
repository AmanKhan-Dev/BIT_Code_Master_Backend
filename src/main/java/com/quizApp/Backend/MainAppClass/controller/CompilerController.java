package com.quizApp.Backend.MainAppClass.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizApp.Backend.MainAppClass.model.CodeRequest;
import com.quizApp.Backend.MainAppClass.model.EvaluationResponse;
import com.quizApp.Backend.MainAppClass.model.TestCase;
import com.quizApp.Backend.MainAppClass.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/compiler")
public class CompilerController {

    @Autowired
    private TestCaseRepository testCaseRepository;

    // Define the URLs for your AI agent endpoints
    private static final String AI_ERROR_REVIEW_URL = "http://54.242.206.201:8000/error_review";
    private static final String AI_CODE_CHECK_URL = "http://54.242.206.201:8000/code_check";
    private static final String AI_CODE_IMPROVE_URL = "http://54.242.206.201:8000/code_improve";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/evaluate")
    public ResponseEntity<EvaluationResponse> evaluateCode(@RequestBody CodeRequest request) {
        EvaluationResponse response = new EvaluationResponse();
        Path tempDir = null;

        try {
            tempDir = Files.createTempDirectory("compile-" + UUID.randomUUID());
            Path sourceFile = tempDir.resolve("source." + (request.getLanguage().equals("C") ? "c" : "cpp"));
            Path outputFile = tempDir.resolve("output");
            Files.write(sourceFile, request.getSourceCode().getBytes());

            // --- Stage 1: Compilation ---
            response.addStage("Compilation", "IN_PROGRESS");
            CompilationResult compilationResult = compile(request.getLanguage(), sourceFile, outputFile);
            if (!compilationResult.success) {
                response.setSuccess(false);
                response.updateLastStage("FAILED");
                response.setOutput("Compilation Error:\n" + compilationResult.output);
                response.setAiReview(getAIForErrorReview(request.getSourceCode(), compilationResult.output));
                return ResponseEntity.ok(response);
            }
            response.updateLastStage("SUCCESS");

           // --- Stage 2: Test Case Validation ---
response.addStage("Test Cases", "IN_PROGRESS");
// REMOVED the Long.parseLong line. We now pass the String directly.
List<TestCase> testCases = testCaseRepository.findTestCasesByQuestionSetIdAndQuestionNo(
        request.getQuestionSetId(), request.getQuestionNo());

            StringBuilder testFailures = new StringBuilder();
            for (TestCase tc : testCases) {
                ExecutionResult result = run(outputFile, tc.getTestCaseInput());
                String normalizedOutput = result.output.replace("\r\n", "\n").trim();
                String expectedOutput = tc.getTestCaseOutput().replace("\r\n", "\n").trim();

                if (result.exitCode != 0 || !normalizedOutput.equals(expectedOutput)) {
                    testFailures.append(String.format("Input: '%s', Expected: '%s', Got: '%s'\n",
                            tc.getTestCaseInput(), expectedOutput, normalizedOutput));
                }
            }

            if (testFailures.length() > 0) {
                response.setSuccess(false);
                response.updateLastStage("FAILED");
                response.setOutput(testFailures.toString());
                return ResponseEntity.ok(response);
            }
            response.updateLastStage("SUCCESS");

            // --- Stage 3: AI Method Check ---
            response.addStage("Method Check", "IN_PROGRESS");
            String methodCheckFeedback = getAIForMethodCheck(request.getSourceCode(), request.getProblemStatement());
            if (methodCheckFeedback.toLowerCase().contains("invalid")) {
                response.setSuccess(false);
                response.updateLastStage("FAILED");
                response.setAiReview(methodCheckFeedback);
                return ResponseEntity.ok(response);
            }
            response.updateLastStage("SUCCESS");
            
            // --- Final Step: Code Improvement ---
            response.addStage("Code Improvement", "IN_PROGRESS");
            response.setAiReview(getAIForCodeImprovement(request.getSourceCode()));
            response.updateLastStage("SUCCESS");
            response.setSuccess(true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setOutput("Internal Server Error: " + e.getMessage());
            if (response.getStages().isEmpty()) {
                response.addStage("System", "FAILED");
            } else {
                response.updateLastStage("FAILED");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            cleanup(tempDir);
        }
    }

    // ===================================
    // AI AGENT INTEGRATION METHODS
    // ===================================

    private String callAiAgent(String url, Object requestBody) throws IOException, InterruptedException {
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(60))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    
    private String getAIForErrorReview(String code, String error) throws IOException, InterruptedException {
        var requestBody = new AiErrorReviewRequest(code, error);
        return callAiAgent(AI_ERROR_REVIEW_URL, requestBody);
    }

    private String getAIForMethodCheck(String code, String problemStatement) throws IOException, InterruptedException {
        var requestBody = new AiCodeCheckRequest(code, problemStatement);
        return callAiAgent(AI_CODE_CHECK_URL, requestBody);
    }

    private String getAIForCodeImprovement(String code) throws IOException, InterruptedException {
        var requestBody = new AiCodeImproveRequest(code);
        return callAiAgent(AI_CODE_IMPROVE_URL, requestBody);
    }

    // DTOs for AI Agent requests (using modern Java Records)
    private record AiErrorReviewRequest(String code, String error) {}
    private record AiCodeCheckRequest(String code, String method) {}
    private record AiCodeImproveRequest(String code) {}

    // ===================================
    // COMPILATION & EXECUTION HELPERS
    // ===================================
    
    private CompilationResult compile(String language, Path sourceFile, Path outputFile) throws IOException, InterruptedException {
        String compiler = language.equals("C") ? "gcc" : "g++";
        ProcessBuilder pb = new ProcessBuilder(compiler, sourceFile.toString(), "-o", outputFile.toString());
        Process p = pb.start();
        String errors = streamToString(p.getErrorStream());
        boolean success = p.waitFor(10, TimeUnit.SECONDS) && p.exitValue() == 0;
        return new CompilationResult(success, errors);
    }

    private ExecutionResult run(Path executableFile, String input) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(executableFile.toString());
        Process p = pb.start();
        if (input != null && !input.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()))) {
                writer.write(input);
            }
        }
        if (!p.waitFor(5, TimeUnit.SECONDS)) {
            p.destroyForcibly();
            return new ExecutionResult(-1, "", "Execution timed out.");
        }
        String output = streamToString(p.getInputStream());
        String errors = streamToString(p.getErrorStream());
        return new ExecutionResult(p.exitValue(), output, errors);
    }

    private String streamToString(InputStream is) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
            return sb.toString().trim();
        }
    }

    private void cleanup(Path tempDir) {
        if (tempDir != null) {
            try {
                Files.walk(tempDir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            } catch (IOException e) {
                System.err.println("Cleanup failed for directory: " + tempDir);
            }
        }
    }

    private static class CompilationResult {
        final boolean success;
        final String output;
        CompilationResult(boolean success, String output) { this.success = success; this.output = output; }
    }

    private static class ExecutionResult {
        final int exitCode;
        final String output;
        final String errors;
        ExecutionResult(int exitCode, String output, String errors) { this.exitCode = exitCode; this.output = output; this.errors = errors; }
    }
}
// package com.quizApp.Backend.MainAppClass.controller;

// import org.springframework.web.bind.annotation.*;
// import com.quizApp.Backend.MainAppClass.model.CodeRequest;
// import com.quizApp.Backend.MainAppClass.model.TestCase;
// import com.quizApp.Backend.MainAppClass.repository.TestCaseRepository;

// import org.springframework.http.ResponseEntity;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;

// import java.io.*;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.Comparator;
// import java.util.List;
// import java.util.UUID;
// import java.util.concurrent.TimeUnit;

// @RestController
// @RequestMapping("/api/compiler")
// public class CompilerController {

//     @Autowired
//     private TestCaseRepository testCaseRepository;

//     @PostMapping("/compile")
//     public ResponseEntity<String> compileCode(@RequestBody CodeRequest codeRequest) {
//         // Create a unique temporary directory for this request to avoid concurrency issues
//         Path tempDir = null;
//         try {
//             tempDir = Files.createTempDirectory("compile-" + UUID.randomUUID().toString());

//             // 1. Prepare file paths within the secure temporary directory
//             String sourceFileName = "source." + (codeRequest.getLanguage().equals("C") ? "c" : "cpp");
//             String outputFileName = "output"; // Linux executables don't need .exe
//             Path sourceFile = tempDir.resolve(sourceFileName);
//             Path outputFile = tempDir.resolve(outputFileName);

//             // 2. Write the source code to the file
//             Files.write(sourceFile, codeRequest.getSourceCode().getBytes());

//             // 3. Compile the code
//             boolean compileSuccess = compile(codeRequest.getLanguage(), sourceFile, outputFile);
//             if (!compileSuccess) {
//                 return ResponseEntity.badRequest().body("Compilation failed.");
//             }

//             // 4. Run the compiled code
//             ExecutionResult result = run(outputFile, codeRequest.getUserInput());

//             if (result.exitCode != 0 || !result.errors.isEmpty()) {
//                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Execution error:\n" + result.errors);
//             }

//             return ResponseEntity.ok(result.output);

//         } catch (IOException | InterruptedException e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error occurred: " + e.getMessage());
//         } finally {
//             // 5. Clean up the temporary directory and its contents
//             if (tempDir != null) {
//                 try {
//                     Files.walk(tempDir)
//                             .sorted(Comparator.reverseOrder())
//                             .map(Path::toFile)
//                             .forEach(File::delete);
//                 } catch (IOException e) {
//                     // Log cleanup failure, but don't send to client
//                     System.err.println("Failed to clean up temporary directory: " + tempDir.toString());
//                 }
//             }
//         }
//     }

//     @PostMapping("/compileTests")
//     public ResponseEntity<String> compileCodeTest(@RequestBody CodeRequest request) {
//         Path tempDir = null;
//         try {
//             tempDir = Files.createTempDirectory("compile-test-" + UUID.randomUUID().toString());

//             String sourceFileName = "source." + (request.getLanguage().equals("C") ? "c" : "cpp");
//             String outputFileName = "output";
//             Path sourceFile = tempDir.resolve(sourceFileName);
//             Path outputFile = tempDir.resolve(outputFileName);

//             Files.write(sourceFile, request.getSourceCode().getBytes());

//             boolean compileSuccess = compile(request.getLanguage(), sourceFile, outputFile);
//             if (!compileSuccess) {
//                 return ResponseEntity.badRequest().body("Compilation failed.");
//             }

//             List<TestCase> testCases = testCaseRepository.findTestCasesByQuestionSetIdAndQuestionNo(request.getQuestionSetId(), request.getQuestionNo());
//             StringBuilder resultBuilder = new StringBuilder();
//             boolean allTestsPassed = true;

//             for (TestCase testCase : testCases) {
//                 ExecutionResult result = run(outputFile, testCase.getTestCaseInput());

//                 String normalizedActualOutput = result.output.replace("\r", "").trim();
//                 String normalizedExpectedOutput = testCase.getTestCaseOutput().replace("\r", "").trim();

//                 if (result.exitCode != 0 || !result.errors.isEmpty()) {
//                     resultBuilder.append("Test case failed for input: ").append(testCase.getTestCaseInput()).append("\n");
//                     resultBuilder.append("Error: ").append(result.errors).append("\n");
//                     allTestsPassed = false;
//                 } else if (!normalizedActualOutput.equals(normalizedExpectedOutput)) {
//                     resultBuilder.append("Test case failed for input: ").append(testCase.getTestCaseInput()).append("\n");
//                     resultBuilder.append("Expected: ").append(normalizedExpectedOutput).append("\n");
//                     resultBuilder.append("Actual: ").append(normalizedActualOutput).append("\n");
//                     allTestsPassed = false;
//                 } else {
//                     resultBuilder.append("Test case passed for input: ").append(testCase.getTestCaseInput()).append("\n");
//                 }
//             }

//             if (allTestsPassed) {
//                 return ResponseEntity.ok("All Hidden Test Cases Passed");
//             } else {
//                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultBuilder.toString());
//             }

//         } catch (IOException | InterruptedException e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error occurred: " + e.getMessage());
//         } finally {
//             if (tempDir != null) {
//                 try {
//                     Files.walk(tempDir)
//                             .sorted(Comparator.reverseOrder())
//                             .map(Path::toFile)
//                             .forEach(File::delete);
//                 } catch (IOException e) {
//                     System.err.println("Failed to clean up temporary directory: " + tempDir.toString());
//                 }
//             }
//         }
//     }

//     private boolean compile(String language, Path sourceFile, Path outputFile) throws IOException, InterruptedException {
//         String compiler = language.equals("C") ? "gcc" : "g++";
//         // Execute command directly, not through "cmd /c"
//         ProcessBuilder compileProcessBuilder = new ProcessBuilder(compiler, sourceFile.toString(), "-o", outputFile.toString());
//         Process compileProcess = compileProcessBuilder.start();
//         return compileProcess.waitFor(10, TimeUnit.SECONDS) && compileProcess.exitValue() == 0;
//     }

//     private ExecutionResult run(Path executableFile, String input) throws IOException, InterruptedException {
//         // Make the file executable on Linux
//         executableFile.toFile().setExecutable(true);

//         ProcessBuilder runProcessBuilder = new ProcessBuilder(executableFile.toString());
//         Process runProcess = runProcessBuilder.start();

//         if (input != null && !input.isEmpty()) {
//             try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
//                 writer.write(input);
//             }
//         }

//         // Timeout to prevent infinite loops in user code
//         if (!runProcess.waitFor(5, TimeUnit.SECONDS)) {
//             runProcess.destroyForcibly();
//             return new ExecutionResult(-1, "", "Execution timed out.");
//         }

//         String output = streamToString(runProcess.getInputStream());
//         String errors = streamToString(runProcess.getErrorStream());

//         return new ExecutionResult(runProcess.exitValue(), output, errors);
//     }

//     private String streamToString(InputStream inputStream) throws IOException {
//         try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
//             StringBuilder sb = new StringBuilder();
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 sb.append(line).append("\n");
//             }
//             return sb.toString();
//         }
//     }

//     // Helper class to hold execution results
//     private static class ExecutionResult {
//         final int exitCode;
//         final String output;
//         final String errors;

//         ExecutionResult(int exitCode, String output, String errors) {
//             this.exitCode = exitCode;
//             this.output = output;
//             this.errors = errors;
//         }
//     }
// }