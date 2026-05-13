package br.com.usuario.controller;

import br.com.infrastructure.mapper.UsuarioMapper;
import br.com.usuario.dto.UsuarioCadastroDTO;
import br.com.usuario.dto.UsuarioListagemDTO;
import br.com.usuario.dto.UsuarioResponseDTO;
import br.com.usuario.entity.Usuario;
import br.com.usuario.service.UsuarioIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioIService usuarioService;
    private final UsuarioMapper mapper;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> save(@RequestBody @Valid UsuarioCadastroDTO dto) {
        // DTO -> Entity
        Usuario usuarioEntity = mapper.toEntity(dto);

        // Service trabalha apenas com Entity
        Usuario criado = usuarioService.save(usuarioEntity);

        // Entity -> DTO de Resposta
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponseDto(criado));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(mapper.toResponseDto(usuarioService.findByEmail(email)));
    }

    @GetMapping("/findall")
    public ResponseEntity<Page<UsuarioListagemDTO>> findAll(
            @PageableDefault(sort = "id") Pageable pageable) {

        Page<Usuario> pagina = usuarioService.findAll(pageable);
        return ResponseEntity.ok(mapper.toPageDto(pagina));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
