package am.hhovhann.healthcare_management.dto;

import org.springframework.lang.NonNull;

public record AuthRequest(@NonNull String username, @NonNull String password) {}
