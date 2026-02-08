package com.projeto.erp.repository;

import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorEndereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColaboradorEnderecoRepository
        extends JpaRepository<ColaboradorEndereco, Long> {

    Optional<ColaboradorEndereco> findByColaborador(Colaborador colaborador);
}
