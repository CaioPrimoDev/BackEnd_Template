package br.com.auth.controller;

import br.com.auth.dto.LoginDTO;
import br.com.auth.dto.LoginResponseDTO;
import br.com.auth.service.AuthService;
import br.com.infrastructure.mapper.UsuarioMapper;
import br.com.usuario.dto.UsuarioCadastroDTO;
import br.com.usuario.entity.PerfilUsuario;
import br.com.usuario.entity.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioMapper mapper; // Usando o novo Mapper


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO dto) {
        LoginResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    // ROTA PÚBLICA (permitAll): Cadastro de clientes comuns
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid UsuarioCadastroDTO dto) {
        // SEGURANÇA: Mesmo que o front envie outro perfil,
        // forçamos CLIENTE nesta rota pública. Já que alguém poderia forçar um ["ADMIN"] pelo JSON
        dto.setPerfis(Set.of(PerfilUsuario.CLIENTE));

        Usuario usuarioEntity = mapper.toEntity(dto);

        authService.register(usuarioEntity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
