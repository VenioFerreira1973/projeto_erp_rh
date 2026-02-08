package com.projeto.erp.repository;

import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorDependentes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ColaboradorDependentesRepository
        extends JpaRepository<ColaboradorDependentes, Long> {

    List<ColaboradorDependentes> findByColaborador(Colaborador colaborador);

    List<ColaboradorDependentes> findAllByColaborador(Colaborador colaborador);

    Optional<ColaboradorDependentes> findByIdAndColaborador(
            Long id,
            Colaborador colaborador
    );
}
