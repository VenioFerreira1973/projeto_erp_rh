package com.projeto.erp.mapper;

import com.projeto.erp.dtos.*;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.modelo.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ColaboradorDependentesMapper {

    default ColaboradorDependentes toEntity(
            ColaboradorDependentesRequestDTO request,
            Colaborador colaborador
    ) {
        return ColaboradorDependentes.criar(colaborador, request);
    }

    default void updateEntity(
            ColaboradorDependentesRequestDTO request,
            @MappingTarget ColaboradorDependentes entity
    ) {
        entity.atualizar(request);
    }

    ColaboradorDependentesResponseDTO toResponse(ColaboradorDependentes entity);

    List<ColaboradorDependentesResponseDTO> toResponseList(List<ColaboradorDependentes> entity);
}
