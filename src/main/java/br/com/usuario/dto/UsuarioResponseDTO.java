package br.com.usuario.dto;

import br.com.usuario.entity.PerfilUsuario;
import br.com.usuario.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    // Todos os dados não-sensíveis para exibição completa
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private boolean status;
    private Set<PerfilUsuario> perfis;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getPessoa().getNome();
        this.cpf = usuario.getPessoa().getCpf();
        this.email = usuario.getPessoa().getEmail();
        this.status = usuario.getStatus(); // ou .getStatus() dependendo do Lombok
        this.perfis = usuario.getPerfis();
    }
}
