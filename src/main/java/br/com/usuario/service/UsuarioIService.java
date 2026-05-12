package br.com.usuario.service;

import br.com.usuario.dto.UsuarioCadastroDTO;
import br.com.usuario.dto.UsuarioResponseDTO;
import br.com.usuario.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface UsuarioIService {
    Usuario save(UsuarioCadastroDTO dto);
    Usuario findByEmail(String email);
    Usuario findByCpf(String cpf);
    Page<Usuario> findAll(Pageable pageable);
    void deleteById(Long id);
    List<UsuarioResponseDTO> listarGestores();
    void alternarStatus(Long id);
}