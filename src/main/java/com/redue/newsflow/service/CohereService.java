package com.redue.newsflow.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
public class CohereService {


    @Value("${cohere.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public CohereService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String resumirTexto(String texto, String idioma) {
        String url = "https://api.cohere.ai/v1/summarize";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String comando = "Resuma o texto no idioma " + idioma + ".";
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("text", texto);
        requestBody.put("length", "medium");
        requestBody.put("format", "paragraph");
        requestBody.put("model", "command");
        requestBody.put("additional_command", comando);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = response.getBody();
            return body != null && body.containsKey("summary") ? body.get("summary").toString() : "Sem resumo disponível.";
        } else {
            throw new RuntimeException("Erro ao resumir texto: " + response.getStatusCode());
        }
    }

}
