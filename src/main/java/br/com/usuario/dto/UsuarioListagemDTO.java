package br.com.usuario.dto;

import br.com.usuario.entity.PerfilUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioListagemDTO {
    private Long id;
    private String cpf;
    private String email;
    private Boolean status;
    private PerfilUsuario perfil;
    //private LocalDateTime dataCadastro;
}
