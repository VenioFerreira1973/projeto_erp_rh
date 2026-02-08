package com.projeto.erp.repository;

import com.projeto.erp.modelo.ColaboradorContrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ColaboradorContratoRepository extends JpaRepository<ColaboradorContrato, Long> {

    @Query("""
    select fc from ColaboradorContrato fc
    left join fetch fc.colaborador
    left join fetch fc.sindicato
    left join fetch fc.salarios
    where fc.id = :id
""")
    Optional<ColaboradorContrato> findByIdComRelacionamentos(@Param("id") Long id);

}
