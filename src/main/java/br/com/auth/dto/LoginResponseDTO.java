package br.com.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String nome;
    private String email; // Útil para mostrar no Header do site "Olá, user@..."
    private Set<String> perfis;
}