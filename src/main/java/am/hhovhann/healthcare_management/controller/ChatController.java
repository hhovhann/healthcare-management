package am.hhovhann.healthcare_management.controller;

import am.hhovhann.healthcare_management.dto.chat.ChatRequestDto;
import am.hhovhann.healthcare_management.dto.chat.ChatResponseDto;
import am.hhovhann.healthcare_management.service.chat.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/ask")
    public ResponseEntity<ChatResponseDto> askQuestion(@RequestBody ChatRequestDto request) {
        try {
            ChatResponseDto response = chatService.processQuestion(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new ChatResponseDto(
                            "I apologize, but I'm having trouble processing your request right now. Please try again later.",
                            true,
                            LocalDateTime.now(),
                            null));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Chat service is running");
    }
}
