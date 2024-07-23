package com.cdyy.ollamaextract.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Service
public class ApiService {
    private static final Logger LOGGER = Logger.getLogger(ApiService.class.getName());
    private final WebClient webClient;

    @Autowired
    private ResourceLoader resourceLoader;

    public ApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://3859kjyg2351.vicp.fun/api").build();
    }

    public Mono<String> generateResponseFromFile(String model, String uploadText) {
        String filePath = "classpath:/configs/" + "prompt-refined.txt";
        try {
            String prompt = new String(Files.readAllBytes(Paths.get(resourceLoader.getResource(filePath).getURI())));
            if (prompt.isEmpty()) {
                LOGGER.warning("Template is empty.");
                return Mono.empty();
            }

            return this.webClient.post()
                    .uri("/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new RequestData(model, prompt + uploadText, "json", "false"))
                    .retrieve()
                    .bodyToMono(String.class);
        } catch (Exception e) {
            LOGGER.severe("Failed to read prompt or send request: " + e.getMessage());
            return Mono.error(e);
        }
    }
    @Data
    private static class RequestData {
        private String model;
        private String prompt;
        private String format;
        private String stream;

        public RequestData(String model, String prompt, String format, String stream) {
            this.model = model;
            this.prompt = prompt;
            this.format = format;
            this.stream = stream;
        }
        // Getters and Setters
    }
}
