package br.com.infrastructure.mapper;

import br.com.usuario.dto.UsuarioCadastroDTO;
import br.com.usuario.dto.UsuarioListagemDTO;
import br.com.usuario.dto.UsuarioResponseDTO;
import br.com.usuario.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "cpf", source = "pessoa.cpf")
    @Mapping(target = "email", source = "pessoa.email")
    UsuarioResponseDTO toResponseDto(Usuario usuario);

    @Mapping(target = "pessoa.cpf", source = "cpf")
    @Mapping(target = "pessoa.email", source = "email")
    Usuario toEntity(UsuarioCadastroDTO dto);

    // Converte a página de entidades para página de DTOs automaticamente
    default Page<UsuarioListagemDTO> toPageDto(Page<Usuario> page) {
        return page.map(this::toListagemDto);
    }

    UsuarioListagemDTO toListagemDto(Usuario usuario);
}
