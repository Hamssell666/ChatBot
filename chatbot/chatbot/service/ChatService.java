package com.uasd.chatbot.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Service
public class ChatService {

    private final String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";
    private final String apiKey = "AIzaSyBgk5x37n5gMGgDzZ4FX2vGpfpvdKA6Zu4"; 
    private final RestTemplate restTemplate;

    public ChatService() {
        this.restTemplate = new RestTemplate();
    }

    public String enviarPregunta(String pregunta) {
        try {
            String urlConKey = apiUrl + "?key=" + apiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> textMap = new HashMap<>();
            textMap.put("text", pregunta);

            Map<String, Object> partsMap = new HashMap<>();
            List<Map<String, Object>> partsList = new ArrayList<>();
            partsList.add(textMap);
            partsMap.put("parts", partsList);

            Map<String, Object> contentsMap = new HashMap<>();
            List<Map<String, Object>> contentsList = new ArrayList<>();
            contentsList.add(partsMap);
            contentsMap.put("contents", contentsList);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(contentsMap, headers);

            Map<String, Object> response = restTemplate.postForObject(urlConKey, entity, Map.class);

            if (response != null && response.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> firstCandidate = candidates.get(0);
                    Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
                    if (content != null && content.containsKey("parts")) {
                        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                        if (!parts.isEmpty()) {
                            return (String) parts.get(0).get("text");
                        }
                    }
                }
            }
            return "No se recibió una respuesta válida de Gemini.";

        } catch (Exception e) {
            return "Error al conectar con Gemini: " + e.getMessage();
        }
    }
}