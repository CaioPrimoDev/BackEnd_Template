package br.com.infrastructure.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Não envia campos nulos no JSON
public class ErrorResponse {
    private final int status;
    private final String title;
    private final String details;
    private final LocalDateTime timestamp;

    // Para erros de validação (@Valid)
    private List<ValidationError> errors;

    // Para debug (só aparece se a flag estiver ativa)
    private String stacktrace;

    @Data
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
    }

    // Construtor rápido para erros simples
    public static ErrorResponse of(int status, String title, String details) {
        return ErrorResponse.builder()
                .status(status)
                .title(title)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
