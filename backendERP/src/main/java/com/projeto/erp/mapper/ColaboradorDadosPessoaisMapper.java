package com.projeto.erp.mapper;

import com.projeto.erp.dtos.ColaboradorDadosPessoaisRequest;
import com.projeto.erp.dtos.ColaboradorDadosPessoaisResponse;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorDadosPessoais;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ColaboradorDadosPessoaisMapper {

    default ColaboradorDadosPessoais toEntity(
            ColaboradorDadosPessoaisRequest request,
            Colaborador colaborador
    ) {
        return ColaboradorDadosPessoais.criar(colaborador, request);
    }

    default void updateEntity(
            ColaboradorDadosPessoaisRequest request,
            @MappingTarget ColaboradorDadosPessoais entity
    ) {
        entity.atualizar(request);
    }

    @Mapping(target = "observacao", source = "observacao")
    @Mapping(target = "statusValidacao", source = "statusValidacao")
    ColaboradorDadosPessoaisResponse toResponse(ColaboradorDadosPessoais entity,
                                                String observacao,
                                                StatusValidacao statusValidacao);
}

