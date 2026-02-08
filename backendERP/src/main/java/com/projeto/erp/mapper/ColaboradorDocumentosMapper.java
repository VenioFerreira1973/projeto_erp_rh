package com.projeto.erp.mapper;


import com.projeto.erp.dtos.ColaboradorDocumentosAnexosResponseDTO;
import com.projeto.erp.dtos.ColaboradorDocumentosRequestDTO;
import com.projeto.erp.dtos.ColaboradorDocumentosResponseDTO;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorDocumentos;
import com.projeto.erp.modelo.ColaboradorDocumentosAnexos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ColaboradorDocumentosMapper {

    default ColaboradorDocumentos toEntity(
            ColaboradorDocumentosRequestDTO request,
            Colaborador colaborador
    ) {
        return ColaboradorDocumentos.criar(colaborador, request);
    }

    default void updateEntity(
            ColaboradorDocumentosRequestDTO request,
            @MappingTarget ColaboradorDocumentos entity
    ) {
        entity.atualizar(request);
    }

    @Mapping(target = "observacao", source = "observacao")
    @Mapping(target = "statusValidacao", source = "statusValidacao")
    ColaboradorDocumentosResponseDTO toResponse(ColaboradorDocumentos entity,
                                                String observacao,
                                                StatusValidacao statusValidacao);
}
