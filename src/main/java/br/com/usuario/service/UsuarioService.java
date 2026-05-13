package br.com.usuario.service;


import br.com.infrastructure.exception.BusinessException;
import br.com.pessoa.repository.PessoaRepository;
import br.com.usuario.dto.UsuarioResponseDTO;
import br.com.usuario.entity.PerfilUsuario;
import br.com.usuario.entity.Usuario;
import br.com.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService implements UsuarioIService {

    private final UsuarioRepository usuarioRepository;
    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        String email = usuario.getPessoa().getEmail();
        String cpf = usuario.getPessoa().getCpf();

        log.info("Iniciando persistência de usuário: {}", email);

        if (usuarioRepository.findByPessoa_Cpf(cpf).isPresent()) {
            throw new BusinessException("CPF já cadastrado.");
        }
        if (usuarioRepository.findByPessoa_Email(email).isPresent()) {
            throw new BusinessException("Email já cadastrado.");
        }

        // Salva a Pessoa primeiro
        pessoaRepository.save(usuario.getPessoa());

        // Codifica a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setStatus(true);

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByPessoa_Email(email)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado com o email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findByCpf(String cpf) {
        return usuarioRepository.findByPessoa_Cpf(cpf)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado com o CPF: " + cpf));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.warn("Tentando deletar usuário ID: {}", id);
        if (!usuarioRepository.existsById(id)) {
            throw new BusinessException("Não foi possível deletar: ID " + id + " inexistente.");
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarGestores() {
        return usuarioRepository.findAllByPerfil(PerfilUsuario.GESTOR)
                .stream()
                .map(UsuarioResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void alternarStatus(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário ID " + id + " não encontrado para alterar status."));

        usuario.setStatus(!usuario.getStatus());
        log.info("Status do usuário {} alterado para: {}", id, usuario.getStatus());
        usuarioRepository.save(usuario);
    }
}
