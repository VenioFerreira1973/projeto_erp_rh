package com.projeto.erp.mapper;

import com.projeto.erp.dtos.ColaboradorDadosBancariosRequestDTO;
import com.projeto.erp.dtos.ColaboradorDadosBancariosResponseDTO;
import com.projeto.erp.dtos.ColaboradorDadosPessoaisResponse;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorDadosBancarios;
import com.projeto.erp.modelo.ColaboradorDadosPessoais;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ColaboradorDadosBancariosMapper {

    default ColaboradorDadosBancarios toEntity(
            ColaboradorDadosBancariosRequestDTO request,
            Colaborador colaborador
    ) {
        return ColaboradorDadosBancarios.criar(colaborador, request);
    }

    default void updateEntity(
            ColaboradorDadosBancariosRequestDTO request,
            @MappingTarget ColaboradorDadosBancarios entity
    ) {
        entity.atualizar(request);
    }

    @Mapping(target = "observacao", source = "observacao")
    @Mapping(target = "statusValidacao", source = "statusValidacao")
    ColaboradorDadosBancariosResponseDTO toResponse(ColaboradorDadosBancarios entity,
                                                    String observacao,
                                                    StatusValidacao statusValidacao);
}
