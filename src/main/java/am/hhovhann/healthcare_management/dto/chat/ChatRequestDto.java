package am.hhovhann.healthcare_management.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequestDto(
        @NotBlank(message = "Question cannot be empty")
                @Size(max = 1000, message = "Question cannot exceed 1000 characters")
                String question,
        String context, // Optional context about the user's role or current page
        String userId) {}
