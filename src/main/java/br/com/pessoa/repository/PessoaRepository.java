package br.com.pessoa.repository;

import br.com.pessoa.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    // Métodos extras de Pessoa (como buscar por CPF isolado) viriam aqui
}
