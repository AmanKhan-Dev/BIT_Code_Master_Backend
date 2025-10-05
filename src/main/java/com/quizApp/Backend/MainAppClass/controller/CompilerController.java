package com.quizApp.Backend.MainAppClass.controller;

import org.springframework.web.bind.annotation.*;
import com.quizApp.Backend.MainAppClass.model.CodeRequest;
import com.quizApp.Backend.MainAppClass.model.TestCase;
import com.quizApp.Backend.MainAppClass.repository.TestCaseRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/compiler")
public class CompilerController {

    @Autowired
    private TestCaseRepository testCaseRepository;

    @PostMapping("/compile")
    public ResponseEntity<String> compileCode(@RequestBody CodeRequest codeRequest) {
        // Create a unique temporary directory for this request to avoid concurrency issues
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("compile-" + UUID.randomUUID().toString());

            // 1. Prepare file paths within the secure temporary directory
            String sourceFileName = "source." + (codeRequest.getLanguage().equals("C") ? "c" : "cpp");
            // Windows executables typically have a .exe extension
            String outputFileName = "output.exe";
            Path sourceFile = tempDir.resolve(sourceFileName);
            Path outputFile = tempDir.resolve(outputFileName);

            // 2. Write the source code to the file
            Files.write(sourceFile, codeRequest.getSourceCode().getBytes());

            // 3. Compile the code
            boolean compileSuccess = compile(codeRequest.getLanguage(), sourceFile, outputFile);
            if (!compileSuccess) {
                return ResponseEntity.badRequest().body("Compilation failed.");
            }

            // 4. Run the compiled code
            ExecutionResult result = run(outputFile, codeRequest.getUserInput());

            if (result.exitCode != 0 || !result.errors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Execution error:\n" + result.errors);
            }

            return ResponseEntity.ok(result.output);

        } catch (IOException | InterruptedException e) {
            // It's good practice to log the exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error occurred: " + e.getMessage());
        } finally {
            // 5. Clean up the temporary directory and its contents
            if (tempDir != null) {
                try {
                    Files.walk(tempDir)
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                } catch (IOException e) {
                    // Log cleanup failure, but don't send to client
                    System.err.println("Failed to clean up temporary directory: " + tempDir.toString());
                }
            }
        }
    }

    @PostMapping("/compileTests")
    public ResponseEntity<String> compileCodeTest(@RequestBody CodeRequest request) {
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("compile-test-" + UUID.randomUUID().toString());

            String sourceFileName = "source." + (request.getLanguage().equals("C") ? "c" : "cpp");
            // Windows executables typically have a .exe extension
            String outputFileName = "output.exe";
            Path sourceFile = tempDir.resolve(sourceFileName);
            Path outputFile = tempDir.resolve(outputFileName);

            Files.write(sourceFile, request.getSourceCode().getBytes());

            boolean compileSuccess = compile(request.getLanguage(), sourceFile, outputFile);
            if (!compileSuccess) {
                return ResponseEntity.badRequest().body("Compilation failed.");
            }

            List<TestCase> testCases = testCaseRepository.findTestCasesByQuestionSetIdAndQuestionNo(request.getQuestionSetId(), request.getQuestionNo());
            StringBuilder resultBuilder = new StringBuilder();
            boolean allTestsPassed = true;

            for (TestCase testCase : testCases) {
                ExecutionResult result = run(outputFile, testCase.getTestCaseInput());

                // Normalize line endings and trim whitespace for robust comparison
                String normalizedActualOutput = result.output.replace("\r\n", "\n").trim();
                String normalizedExpectedOutput = testCase.getTestCaseOutput().replace("\r\n", "\n").trim();

                if (result.exitCode != 0 || !result.errors.isEmpty()) {
                    resultBuilder.append("Test case failed for input: ").append(testCase.getTestCaseInput()).append("\n");
                    resultBuilder.append("Error: ").append(result.errors).append("\n");
                    allTestsPassed = false;
                } else if (!normalizedActualOutput.equals(normalizedExpectedOutput)) {
                    resultBuilder.append("Test case failed for input: ").append(testCase.getTestCaseInput()).append("\n");
                    resultBuilder.append("Expected: ").append(normalizedExpectedOutput).append("\n");
                    resultBuilder.append("Actual: ").append(normalizedActualOutput).append("\n");
                    allTestsPassed = false;
                } else {
                    resultBuilder.append("Test case passed for input: ").append(testCase.getTestCaseInput()).append("\n");
                }
            }

            if (allTestsPassed) {
                return ResponseEntity.ok("All Hidden Test Cases Passed");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultBuilder.toString());
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error occurred: " + e.getMessage());
        } finally {
            if (tempDir != null) {
                try {
                    Files.walk(tempDir)
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                } catch (IOException e) {
                    System.err.println("Failed to clean up temporary directory: " + tempDir.toString());
                }
            }
        }
    }

    private boolean compile(String language, Path sourceFile, Path outputFile) throws IOException, InterruptedException {
        String compiler = language.equals("C") ? "gcc" : "g++";
        ProcessBuilder compileProcessBuilder = new ProcessBuilder(compiler, sourceFile.toString(), "-o", outputFile.toString());
        
        // Redirect error stream to inherit from the parent process for better logging
        compileProcessBuilder.redirectErrorStream(true);

        Process compileProcess = compileProcessBuilder.start();
        return compileProcess.waitFor(10, TimeUnit.SECONDS) && compileProcess.exitValue() == 0;
    }

    private ExecutionResult run(Path executableFile, String input) throws IOException, InterruptedException {
        // The line `executableFile.toFile().setExecutable(true);` is a POSIX permission setting
        // and is not needed or effective on Windows. The OS determines executability
        // by the .exe extension.

        ProcessBuilder runProcessBuilder = new ProcessBuilder(executableFile.toString());
        Process runProcess = runProcessBuilder.start();

        if (input != null && !input.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
                writer.write(input);
            }
        }

        // Timeout to prevent infinite loops in user code
        if (!runProcess.waitFor(5, TimeUnit.SECONDS)) {
            runProcess.destroyForcibly();
            return new ExecutionResult(-1, "", "Execution timed out.");
        }

        String output = streamToString(runProcess.getInputStream());
        String errors = streamToString(runProcess.getErrorStream());

        return new ExecutionResult(runProcess.exitValue(), output, errors);
    }

    private String streamToString(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

    // Helper class to hold execution results
    private static class ExecutionResult {
        final int exitCode;
        final String output;
        final String errors;

        ExecutionResult(int exitCode, String output, String errors) {
            this.exitCode = exitCode;
            this.output = output;
            this.errors = errors;
        }
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