package br.com.usuario.entity;

import br.com.infrastructure.entity.PersistenceEntity;
import br.com.pessoa.entity.Pessoa;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "usuario")
public class Usuario extends PersistenceEntity implements UserDetails {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "perfil")
    private Set<PerfilUsuario> perfis = new HashSet<>();

    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
    private String senha;
    private Boolean status = true;
    private LocalDateTime dataCadastro;
    private LocalDateTime ultimoLogin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Converte seus Perfis (Enum) para o formato que o Spring entende
        return perfis.stream()
                .map(perfil -> new SimpleGrantedAuthority(perfil.getAuthority()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() { // Classe obrigatoria, já que há um contrato com UserDetails
        // O "nome de usuário" pro Spring será o email da Pessoa
        return this.pessoa.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return this.status; }
}