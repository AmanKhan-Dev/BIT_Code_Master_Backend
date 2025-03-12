package com.quizApp.Backend.MainAppClass.controller;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.concurrent.TimeUnit;

import java.io.*;

public class CCodeExecutionWebSocketHandler extends TextWebSocketHandler {
    private Process process;
    private BufferedWriter writer;
    private BufferedReader reader;
    private Thread outputThread;
    private WebSocketSession currentSession;
    private volatile boolean isSessionOpen = false;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.currentSession = session;
        isSessionOpen = true;
        sendMessage(session, "WebSocket Connected! Ready to execute code.");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String clientMessage = message.getPayload();

        if (clientMessage.startsWith("RUN_CODE")) {
            String code = clientMessage.substring(9); // Extract code after "RUN_CODE "
            executeCode(session, code);
        } else {
            // Handle user input dynamically
            if (writer != null) {
                try {
                    writer.write(clientMessage + "\n");
                    writer.flush();
                } catch (IOException e) {
                    sendMessage(session, "Error writing input: " + e.getMessage());
                }
            }
        }
    }

    private void executeCode(WebSocketSession session, String code) throws Exception {
        File tempCFile = File.createTempFile("temp", ".c");
        try (FileWriter fileWriter = new FileWriter(tempCFile)) {
            fileWriter.write(code);
        }

        // Ensure the previous process is terminated before deleting the executable
        if (process != null) {
            process.destroy();
            process.waitFor(500, TimeUnit.MILLISECONDS); // Wait for process to release resources
        }

// Attempt to delete the old executable
        File executable = new File("tempExecutable.exe");
        if (executable.exists()) {
            boolean deleted = executable.delete();
            if (!deleted) {
                Thread.sleep(500); // Wait a bit and retry
                deleted = executable.delete();
                if (!deleted) {
                    session.sendMessage(new TextMessage("Error: Unable to delete previous executable. Try again."));
                    return;
                }
            }
        }


        // Compile C code
        Process compileProcess = new ProcessBuilder("gcc", tempCFile.getAbsolutePath(), "-o", "tempExecutable.exe")
                .redirectErrorStream(true)
                .start();

        try (BufferedReader compileReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()))) {
            StringBuilder compileErrors = new StringBuilder();
            String line;
            while ((line = compileReader.readLine()) != null) {
                compileErrors.append(line).append("\n");
            }

            compileProcess.waitFor();

            if (compileProcess.exitValue() != 0) {
                sendMessage(session, "Compilation Error:\n" + compileErrors.toString().trim());
                return;
            }
        }

        sendMessage(session, "Compiled Successfully!\nRunning program...\n");

        // Run compiled C program
        process = new ProcessBuilder("./tempExecutable.exe")
                .redirectErrorStream(true)
                .start();

        writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        // Start a new thread to send output to the client
        outputThread = new Thread(() -> {
            try {
                String outputLine;
                while (isSessionOpen && (outputLine = reader.readLine()) != null) {
                    sendMessage(session, outputLine);
                }
            } catch (IOException e) {
                if (isSessionOpen) {
                    sendMessage(session, "Error reading output: " + e.getMessage());
                }
            }
        });

        outputThread.start();
    }


    private void sendMessage(WebSocketSession session, String message) {
        try {
            if (session != null && session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        } catch (IOException e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        isSessionOpen = false;
        if (process != null) process.destroy();
        if (outputThread != null) outputThread.interrupt();
    }
}
