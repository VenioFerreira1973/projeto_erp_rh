package com.projeto.erp.repository;

import com.projeto.erp.modelo.ColaboradorLotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ColaboradorLotacaoRepository extends JpaRepository<ColaboradorLotacao, Long> {

    List<ColaboradorLotacao> findByDepartamentoId(Long departamentoId);

    List<ColaboradorLotacao> findByCargoId(Long cargoId);

    List<ColaboradorLotacao> findByGestorId(Long gestorId);

    @Query("""
    select fc from ColaboradorLotacao fc
    left join fetch fc.colaborador
    left join fetch fc.centroCusto
    left join fetch fc.cargo
    left join fetch fc.departamento
    left join fetch fc.gestor
    where fc.id = :id
""")
    Optional<ColaboradorLotacao> findByIdComRelacionamentos(@Param("id") Long id);

}
