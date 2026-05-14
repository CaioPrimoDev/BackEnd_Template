package br.com.infrastructure.config;

import br.com.pessoa.entity.Pessoa;
import br.com.pessoa.repository.PessoaRepository;
import br.com.usuario.entity.PerfilUsuario;
import br.com.usuario.entity.Usuario;
import br.com.usuario.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class TestConfig implements CommandLineRunner {

    private final UsuarioRepository repository;
    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public void run(String... args) {
        // Busca pelo e-mail da pessoa vinculada ao usuário
        // Ajuste o método conforme seu repositório (ex: findByPessoaEmail)
        if (repository.findAll().stream().noneMatch(u -> "admin@email.com".equals(u.getPessoa().getEmail()))) {

            Pessoa pessoa = new Pessoa();
            pessoa.setEmail("admin@email.com");
            pessoa.setCpf("1234567890");
            Pessoa pessoaSalva = pessoaRepository.save(pessoa);

            Usuario admin = new Usuario();
            admin.setSenha(encoder.encode("123456"));
            admin.setStatus(true);
            admin.setPerfis(Set.of(PerfilUsuario.ADMIN));
            admin.setPessoa(pessoaSalva); // O vínculo crucial!

            repository.save(admin);
            System.out.println("----------------------------------------------");
            System.out.println(">>> USUÁRIO DE TESTE CRIADO: " + pessoa.getEmail() + "/ " + "123456");
            System.out.println("----------------------------------------------");
        }
    }
}
