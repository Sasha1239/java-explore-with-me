package ru.practicum.exceptions;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ApiError {
    private final List<String> errors;
    private final String message;
    private final String reason;
    private final ErrorStatus status;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
