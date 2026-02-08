package com.projeto.erp.mapper;

import com.projeto.erp.dtos.ColaboradorDocumentosResponseDTO;
import com.projeto.erp.dtos.ColaboradorEnderecoRequest;
import com.projeto.erp.dtos.ColaboradorEnderecoResponse;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorDocumentos;
import com.projeto.erp.modelo.ColaboradorEndereco;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ColaboradorEnderecoMapper {

    default ColaboradorEndereco toEntity(
            ColaboradorEnderecoRequest request,
            Colaborador colaborador
    ) {
        return ColaboradorEndereco.criar(colaborador, request);
    }

    default void updateEntity(
            ColaboradorEnderecoRequest request,
            @MappingTarget ColaboradorEndereco entity
    ) {
        entity.atualizar(request);
    }

    @Mapping(target = "observacao", source = "observacao")
    @Mapping(target = "statusValidacao", source = "statusValidacao")
    ColaboradorEnderecoResponse toResponse(ColaboradorEndereco entity,
                                           String observacao,
                                           StatusValidacao statusValidacao);
}

