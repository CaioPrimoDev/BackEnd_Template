package br.com.usuario.repository;

import br.com.usuario.entity.PerfilUsuario;
import br.com.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // O underscore '_' ajuda o Spring a entender que deve entrar em 'pessoa' e buscar 'cpf'
    // Isso evita conflitos se houvesse um campo chamado 'pessoacpf' no Usuario
    Optional<Usuario> findByPessoa_Cpf(String cpf);

    Optional<Usuario> findByPessoa_Email(String email);

    Optional<Usuario> findByPessoa_CpfAndPessoa_Email(String cpf, String email);

    // Ajuste na Query para lidar com ElementCollection de Enums
    // Usamos 'MEMBER OF' ou o JOIN explícito que você fez (ambos funcionam, mas MEMBER OF é mais legível para coleções)
    @Query("SELECT u FROM Usuario u WHERE :perfil MEMBER OF u.perfis")
    List<Usuario> findAllByPerfil(@Param("perfil") PerfilUsuario perfil);
}
