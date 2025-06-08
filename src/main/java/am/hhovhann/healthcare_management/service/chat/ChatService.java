package am.hhovhann.healthcare_management.service.chat;

import am.hhovhann.healthcare_management.dto.chat.ChatRequestDto;
import am.hhovhann.healthcare_management.dto.chat.ChatResponseDto;

public interface ChatService {
    ChatResponseDto processQuestion(ChatRequestDto request);
}
