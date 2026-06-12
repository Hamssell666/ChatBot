package com.uasd.chatbot.controller;
import com.uasd.chatbot.model.ChatRequest;
import com.uasd.chatbot.model.ChatResponse;
import com.uasd.chatbot.service.ChatService;
import com.uasd.chatbot.model.ChatRequest;
import com.uasd.chatbot.model.ChatResponse;
import com.uasd.chatbot.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*") // Clave para que tu HTML local pueda comunicarse con Java
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/ask")
    public ChatResponse askChatbot(@RequestBody ChatRequest request) {
        // Validación de datos de entrada (Manejo de errores para la nota)
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            return new ChatResponse("Por favor, escribe una pregunta válida.");
        }

        // Llamamos al servicio de Gemini
        String aiAnswer = chatService.generateResponse(request.getMessage());
        return new ChatResponse(aiAnswer);
    }
}