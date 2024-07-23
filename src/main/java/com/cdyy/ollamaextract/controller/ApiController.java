package com.cdyy.ollamaextract.controller;

import com.cdyy.ollamaextract.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ApiController {

    @Autowired
    private ApiService apiService;

    @GetMapping("/generate-from-text")
    public Mono<String> generateFromFile(@RequestParam String model, @RequestParam String uploadText) {
        return apiService.generateResponseFromFile(model, uploadText);
    }

    @GetMapping("/generate")
    public Mono<String> generate(@RequestParam String model) {
        System.out.println("model = " + model);return null;
    }
}

