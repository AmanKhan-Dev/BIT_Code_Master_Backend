package com.quizApp.Backend.MainAppClass.controller;

import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class CCodeExecutionController {

    private Process runningProcess;
    private BufferedWriter processInputWriter;
    private BufferedReader processOutputReader;
    private StringBuilder executionOutput = new StringBuilder();

    @PostMapping("/runner")
    public Map<String, String> executeCode(@RequestBody Map<String, String> requestData) {
        Map<String, String> response = new HashMap<>();
        String code = requestData.get("code");

        try {
            // ✅ Create a temporary directory for execution
            Path tempDir = Files.createTempDirectory("c-execution");
            File cFile = new File(tempDir.toFile(), "program.c");

            // ✅ Write C code to file
            try (FileWriter writer = new FileWriter(cFile)) {
                writer.write(code);
            }

            // ✅ Compile the C program
            Process compileProcess = new ProcessBuilder("gcc", cFile.getAbsolutePath(), "-o", tempDir.resolve("program").toString()).start();
            compileProcess.waitFor();

            if (compileProcess.exitValue() != 0) {
                response.put("output", "❌ Compilation Error");
                return response;
            }

            // ✅ Run compiled program in interactive mode
            runningProcess = new ProcessBuilder(tempDir.resolve("program").toString()).start();
            processInputWriter = new BufferedWriter(new OutputStreamWriter(runningProcess.getOutputStream()));
            processOutputReader = new BufferedReader(new InputStreamReader(runningProcess.getInputStream()));

            // ✅ Read output in a separate thread to keep it real-time
            new Thread(() -> {
                try {
                    String line;
                    while ((line = processOutputReader.readLine()) != null) {
                        synchronized (executionOutput) {
                            executionOutput.append(line).append("\n");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            response.put("output", "🟢 Execution Started. Waiting for input...");
            return response;

        } catch (Exception e) {
            response.put("output", "❌ Execution Error: " + e.getMessage());
            return response;
        }
    }

    @PostMapping("/input")
    public Map<String, String> sendInput(@RequestBody Map<String, String> requestData) {
        Map<String, String> response = new HashMap<>();
        String userInput = requestData.get("input");

        try {
            if (runningProcess == null || processInputWriter == null) {
                response.put("output", "❌ No Active Execution");
                return response;
            }

            // ✅ Send input to running process
            processInputWriter.write(userInput + "\n");
            processInputWriter.flush();

            // ✅ Get updated output
            synchronized (executionOutput) {
                response.put("output", executionOutput.toString());
            }

        } catch (IOException e) {
            response.put("output", "❌ Input Error: " + e.getMessage());
        }

        return response;
    }
}
