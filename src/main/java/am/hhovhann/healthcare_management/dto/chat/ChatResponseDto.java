package am.hhovhann.healthcare_management.dto.chat;

import java.time.LocalDateTime;

public record ChatResponseDto(
        String response, boolean error, LocalDateTime timestamp, String suggestion) {}
