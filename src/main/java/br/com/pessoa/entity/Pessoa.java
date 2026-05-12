package br.com.pessoa.entity;

import br.com.infrastructure.entity.PersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "pessoa")
public class Pessoa extends PersistenceEntity {
    private String cpf;
    private String email;
}
