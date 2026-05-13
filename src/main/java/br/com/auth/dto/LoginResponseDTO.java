package br.com.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String email; // Útil para mostrar no Header do site "Olá, user@..."
}