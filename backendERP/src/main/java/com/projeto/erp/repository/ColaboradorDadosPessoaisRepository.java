package com.projeto.erp.repository;

import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorDadosPessoais;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColaboradorDadosPessoaisRepository
        extends JpaRepository<ColaboradorDadosPessoais, Long> {

    Optional<ColaboradorDadosPessoais> findByColaborador(Colaborador colaborador);
}
