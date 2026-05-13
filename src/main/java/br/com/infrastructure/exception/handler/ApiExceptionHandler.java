package br.com.infrastructure.exception.handler;

import br.com.infrastructure.exception.BusinessException;
import br.com.infrastructure.exception.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List; // FALTAVA ESTE
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @Value("${app.error.print-stacktrace:false}")
    private boolean printStackTrace;

    // 1. Erros de Regra de Negócio (400)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(
                        HttpStatus.BAD_REQUEST.value(),
                        "Erro de Regra de Negócio",
                        ex.getMessage()
                ));
    }

    // 2. Erros de Validação (Onde você usa @Valid no DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<ErrorResponse.ValidationError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ErrorResponse.ValidationError(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Campos Inválidos")
                .details("Um ou mais campos estão incorretos")
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    // 3. Erro de Acesso Negado (Segurança - 403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(
                        HttpStatus.FORBIDDEN.value(),
                        "Acesso Negado",
                        "Você não tem permissão para acessar este recurso."
                ));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(
                        HttpStatus.NOT_FOUND.value(),
                        "Recurso não encontrado",
                        "O caminho '" + ex.getResourcePath() + "' não existe."
                ));
    }

    // 4. CATCH-ALL (Erro 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ErrorResponse response = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno",
                "Ocorreu um erro inesperado no servidor."
        );

        if (printStackTrace) {
            response.setStacktrace(ExceptionUtils.getStackTrace(ex));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}