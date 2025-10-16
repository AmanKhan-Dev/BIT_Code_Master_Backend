package com.quizApp.Backend.MainAppClass.Service;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class FastApiService {

    
    private final WebClient webClient = WebClient.create("https://1a2b-34-56-78-90.ngrok-free.app");


    public void sendErrorToFastApi() {
    
        String traceback = "Traceback (most recent call last):\n" +
                           "  File \"main.py\", line 5, in <module>\n" +
                           "    print(my_list[3])\n" +
                           "IndexError: list index out of range";

        Map<String, String> errorPayload = new HashMap<>();
        errorPayload.put("error", traceback);

        String response = webClient.post()
                .uri("/report-error")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(errorPayload)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("Response from FastAPI server: " + response);
    }
}
