package br.com.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "Login é obrigatório")
    private String login; // Recebe CPF ou Email

    @NotBlank(message = "Senha é obrigatória")
    private String senha;
}
