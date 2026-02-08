package com.projeto.erp.mapper;

import com.projeto.erp.dtos.ColaboradorDependentesResponseDTO;
import com.projeto.erp.dtos.ColaboradorDocumentosAnexosRequestDTO;
import com.projeto.erp.dtos.ColaboradorDocumentosAnexosResponseDTO;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorDependentes;
import com.projeto.erp.modelo.ColaboradorDocumentosAnexos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ColaboradorDocumentosAnexosMapper {

    default ColaboradorDocumentosAnexos toEntity(
            ColaboradorDocumentosAnexosRequestDTO request,
            Colaborador colaborador
    ) {
        return ColaboradorDocumentosAnexos.criar(colaborador, request);
    }

    default void updateEntity(
            ColaboradorDocumentosAnexosRequestDTO request,
            @MappingTarget ColaboradorDocumentosAnexos entity
    ) {
        entity.atualizar(request);
    }

    ColaboradorDocumentosAnexosResponseDTO toResponse(ColaboradorDocumentosAnexos entity);
}

