package br.com.auth.service;

import br.com.auth.dto.LoginDTO;
import br.com.auth.dto.LoginResponseDTO;
import br.com.infrastructure.exception.BusinessException;
import br.com.infrastructure.security.TokenService;
import br.com.usuario.entity.Usuario;
import br.com.usuario.repository.UsuarioRepository;
import br.com.usuario.service.UsuarioIService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioIService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // Para validar a senha no login
    private final TokenService tokenService;

    /**
     * Lógica de Login Híbrido (CPF ou Email)
     */
    public LoginResponseDTO login(LoginDTO dto) {
        Usuario usuario;

        // Logica para descobrir se é Email ou CPF
        if (dto.getLogin().contains("@")) {
            usuario = usuarioService.findByEmail(dto.getLogin());
        } else {
            // Garante que só tem números para buscar no banco
            String cpfLimpo = dto.getLogin().replaceAll("\\D", "");
            usuario = usuarioService.findByCpf(cpfLimpo);
        }

        // Verifica a senha (Senha digitada vs Hash do banco, já que ela foi encripada antes de ser salva)
        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new BusinessException("Senha inválidas.");
        }

        // Gera o Token (Simulado com UUID por enquanto)
        String token = tokenService.generateToken(usuario);

        // Mapeia o Set<PerfilUsuario> para um Set<String> contendo as roles (ex: "ROLE_CLIENTE")
        java.util.Set<String> perfisString = usuario.getPerfis().stream()
                .map(perfil -> perfil.getAuthority()) // Retorna "ROLE_ADMIN", "ROLE_CLIENTE", etc.
                .collect(java.util.stream.Collectors.toSet());

        // Atualizando ultimo login
        usuario.setUltimoLogin(LocalDateTime.now());

        usuarioRepository.save(usuario);

        // Retorna o DTO com Token e talvez o Perfil principal
        return new LoginResponseDTO(token, usuario.getPessoa().getNome(), usuario.getPessoa().getEmail(), perfisString);
    }

    /**
     * Registro público (Apenas delega para o UsuarioService)
     */
    @Transactional
    public void register(Usuario user) {
        usuarioService.save(user);
    }
}
