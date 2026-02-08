package com.projeto.erp.mapper;

import com.projeto.erp.dtos.OnboardingValidacoesRequestDTO;
import com.projeto.erp.dtos.OnboardingValidacoesResponseDTO;
import com.projeto.erp.modelo.OnboardingValidacoes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OnboardingValidacoesMapper {

    default OnboardingValidacoes toEntity(
            OnboardingValidacoesRequestDTO request,
            OnboardingValidacoes onboardingValidacoes
    ) {
        return OnboardingValidacoes.criar(onboardingValidacoes, request);
    }

    default void updateEntity(
            OnboardingValidacoesRequestDTO request,
            @MappingTarget OnboardingValidacoes entity
    ) {
        entity.atualizar(request);
    }

    OnboardingValidacoesResponseDTO toResponse(OnboardingValidacoes entity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "colaboradorId", target = "colaboradorId")
    @Mapping(source = "onboardingValidacaoStep", target = "onboardingValidacaoStep")
    @Mapping(source = "statusValidacao", target = "statusValidacao")
    @Mapping(source = "observacao", target = "observacao")
    @Mapping(source = "validadoPor", target = "validadoPor")
    @Mapping(source = "validadoEm", target = "validadoEm")
    List<OnboardingValidacoesResponseDTO> toResponse(List<OnboardingValidacoes> entities);
}
