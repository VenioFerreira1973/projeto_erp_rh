package com.projeto.erp.repository;

import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorDadosBancarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColaboradorDadosBancariosRepository
        extends JpaRepository<ColaboradorDadosBancarios, Long> {

    Optional<ColaboradorDadosBancarios> findByColaborador(Colaborador colaborador);
}
