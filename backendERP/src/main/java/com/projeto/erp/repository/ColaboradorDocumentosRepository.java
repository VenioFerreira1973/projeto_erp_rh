package com.projeto.erp.repository;

import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorDocumentos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColaboradorDocumentosRepository
        extends JpaRepository<ColaboradorDocumentos, Long> {

    Optional<ColaboradorDocumentos> findByColaborador(Colaborador colaborador);

}
