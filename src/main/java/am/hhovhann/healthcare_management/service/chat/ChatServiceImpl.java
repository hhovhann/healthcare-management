package am.hhovhann.healthcare_management.service.chat;

import am.hhovhann.healthcare_management.dto.chat.ChatRequestDto;
import am.hhovhann.healthcare_management.dto.chat.ChatResponseDto;
import am.hhovhann.healthcare_management.enums.AiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService {

    @Value("${ai.api.key:your-api-key}")
    private String apiKey;

    @Value("${ai.api.provider:OPEN_AI}")
    private AiModel aiProvider;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ChatServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public ChatResponseDto processQuestion(ChatRequestDto request) {
        String enhancedPrompt = buildHealthcarePrompt(request);

        try {
            return switch (aiProvider) {
                case OPEN_AI -> callOpenAI(enhancedPrompt);
                case CLAUD_AI -> callClaude(enhancedPrompt);
                default -> createFallbackResponse(request.question());
            };
        } catch (Exception e) {
            return createErrorResponse();
        }
    }

    private String buildHealthcarePrompt(ChatRequestDto request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a healthcare management assistant. ");
        prompt.append(
                "You help with healthcare administrative tasks, patient management, scheduling, and general healthcare queries. ");
        prompt.append("Always provide professional, accurate, and helpful responses. ");
        prompt.append(
                "If you're unsure about medical advice, recommend consulting with healthcare professionals. ");

        if (request.context() != null) {
            prompt.append("Context: ").append(request.context()).append(" ");
        }

        prompt.append("\n\nUser Question: ").append(request.question());

        return prompt.toString();
    }

    private ChatResponseDto callOpenAI(String prompt) throws Exception {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        requestBody.put("max_tokens", 500);
        requestBody.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        JsonNode jsonResponse = objectMapper.readTree(response.getBody());
        String aiResponse =
                jsonResponse.path("choices").get(0).path("message").path("content").asText();

        return new ChatResponseDto(
                aiResponse, // response
                false, // error (set this accordingly)
                LocalDateTime.now(), // timestamp
                generateSuggestion(aiResponse) // suggestion
                );
    }

    private ChatResponseDto callClaude(String prompt) throws Exception {
        String url = "https://api.anthropic.com/v1/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "claude-3-sonnet-20240229");
        requestBody.put("max_tokens", 500);
        requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        JsonNode jsonResponse = objectMapper.readTree(response.getBody());
        String aiResponse = jsonResponse.path("content").get(0).path("text").asText();

        return new ChatResponseDto(
                aiResponse, false, LocalDateTime.now(), generateSuggestion(aiResponse));
    }

    private ChatResponseDto createFallbackResponse(String question) {
        // Simple keyword-based responses for common healthcare management queries
        String aiResponse;
        String lowerQuestion = question.toLowerCase();

        if (lowerQuestion.contains("patient") && lowerQuestion.contains("add")) {
            aiResponse =
                    "To add a new patient, navigate to the 'Add Patient' section and fill in the required information including personal details, medical history, and insurance information. Make sure all mandatory fields are completed before saving.";
        } else if (lowerQuestion.contains("schedule") || lowerQuestion.contains("appointment")) {
            aiResponse =
                    "For scheduling appointments, use the 'Schedule' feature. Select the patient, choose an available doctor, pick a date and time, and specify the appointment type. You can also set reminders and add notes if needed.";
        } else if (lowerQuestion.contains("report")) {
            aiResponse =
                    "Reports can be generated from the 'Reports' section. You can create patient reports, appointment summaries, billing reports, and analytics. Choose your filters and date range to customize the report.";
        } else if (lowerQuestion.contains("patient")
                && (lowerQuestion.contains("view") || lowerQuestion.contains("find"))) {
            aiResponse =
                    "To view patients, go to 'View Patients' where you can search by name, ID, or other criteria. You can filter by status, doctor, or date range to find specific patients quickly.";
        } else {
            aiResponse =
                    "I'm here to help with healthcare management tasks like patient registration, appointment scheduling, generating reports, and system navigation. Could you please provide more specific details about what you need assistance with?";
        }

        return new ChatResponseDto(
                aiResponse, // response
                false, // error (set this accordingly)
                LocalDateTime.now(), // timestamp
                "Try asking about: 'How to add a patient', 'Schedule an appointment', 'Generate reports', or 'Find a patient'" // suggestion
                );
    }

    private ChatResponseDto createErrorResponse() {
        return new ChatResponseDto(
                "I'm experiencing technical difficulties right now. Please try again in a moment, or contact your system administrator if the problem persists.", // response
                true, // error (set this accordingly)
                LocalDateTime.now(), // timestamp
                null // suggestion
                );
    }

    private String generateSuggestion(String response) {
        // Generate contextual follow-up suggestions based on the response
        if (response.toLowerCase().contains("patient")) {
            return "Would you like to know about patient management features?";
        } else if (response.toLowerCase().contains("appointment")) {
            return "Need help with appointment scheduling or management?";
        } else if (response.toLowerCase().contains("report")) {
            return "Want to learn about different types of reports available?";
        }
        return "How else can I assist you with healthcare management?";
    }
}
