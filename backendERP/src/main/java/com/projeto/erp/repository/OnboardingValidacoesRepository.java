package com.projeto.erp.repository;

import com.projeto.erp.dtos.OnboardingValidacoesResponseDTO;
import com.projeto.erp.enumeracoes.OnboardingValidacaoStep;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.modelo.OnboardingValidacoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OnboardingValidacoesRepository
        extends JpaRepository<OnboardingValidacoes, Long> {

    Optional<OnboardingValidacoes> findByColaboradorIdAndOnboardingValidacaoStep(
            Long colaboradorId,
            OnboardingValidacaoStep onboardingValidacaoStep
    );

    List<OnboardingValidacoes> findAllByColaboradorId(Long colaboradorId);

    List<OnboardingValidacoes> findAllByStatusValidacao(StatusValidacao statusValidacao);

    List<OnboardingValidacoes> findAllByColaboradorIdAndStatusValidacao(
            Long colaboradorId,
            StatusValidacao status
    );

    @Query("""
    select new com.projeto.erp.dtos.OnboardingValidacoesResponseDTO(
        v.id,
        v.colaboradorId,
        c.nome,
        v.onboardingValidacaoStep,
        v.statusValidacao,
        v.observacao,
        cast(v.validadoPor as string),
        v.validadoEm
    )
    from OnboardingValidacoes v
    join Colaborador c on c.id = v.colaboradorId
    where v.statusValidacao = :status
""")
    List<OnboardingValidacoesResponseDTO> findPendentesComNome(
            @Param("status") StatusValidacao status
    );

    OnboardingValidacoes findByColaboradorId(Long colaboradorId);

}
