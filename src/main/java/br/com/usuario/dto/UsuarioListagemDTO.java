package br.com.usuario.dto;

import br.com.usuario.entity.PerfilUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioListagemDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private Boolean status;
    private Set<PerfilUsuario> perfis;
    //private LocalDateTime dataCadastro;
}
