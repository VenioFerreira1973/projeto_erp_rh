package com.projeto.erp.mapper;

import com.projeto.erp.dtos.*;
import com.projeto.erp.modelo.*;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface ColaboradorContratoMapper {

    /* ===================== RESPONSE ===================== */

    @Mapping(target = "colaboradorId", source = "colaborador.id")
    @Mapping(target = "colaboradorNome", source = "colaborador.nome")
    @Mapping(target = "matricula", source = "colaborador.matricula")
    @Mapping(target = "empresaId", source = "empresa.id")
    @Mapping(target = "empresaNome", source = "empresa.nomeFantasia")
    @Mapping(target = "sindicato", source = "sindicato", qualifiedByName = "sindicatoToString")
    @Mapping(target = "contratoStatus", source = "contratoStatus")
    ColaboradorContratoDTOResponse toResponse(ColaboradorContrato entity);


    /* ===================== REQUEST → ENTITY ===================== */

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)

    // Relacionamentos resolvidos no service
    @Mapping(target = "colaborador", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "sindicato", ignore = true)
    @Mapping(target = "salarios", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAlteracao", ignore = true)
    @Mapping(target = "contratoStatus", ignore = true)
    ColaboradorContrato toEntity(ColaboradorContratoCreateDTO dto);


    /* ===================== UPDATE ===================== */

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "colaborador", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "sindicato", ignore = true)
    @Mapping(target = "salarios", ignore = true)
    @Mapping(target = "contratoStatus", ignore = true)
    void updateEntityFromDTO(
            ColaboradorContratoDTORequest dto,
            @MappingTarget ColaboradorContrato entity
    );


    /* ================= CREATE ================= */

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "colaborador", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "salarios", ignore = true)
    @Mapping(target = "contratoStatus", ignore = true)
    ColaboradorContrato fromCreateDTO(ColaboradorContratoCreateDTO dto);

    /* ================= UPDATE ================= */

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromUpdateDTO(
            ColaboradorContratoUpdateDTO dto,
            @MappingTarget ColaboradorContrato entity
    );

    /* ================= RESPONSE ================= */

    @Mapping(target = "colaboradorId", source = "colaborador.id")
    @Mapping(target = "colaboradorNome", source = "colaborador.nome")
    @Mapping(target = "matricula", source = "colaborador.matricula")
    @Mapping(target = "empresaId", source = "empresa.id")
    @Mapping(target = "empresaNome", source = "empresa.nomeFantasia")
    @Mapping(target = "sindicato", source = "sindicato.nome")
    ColaboradorContratoDTOResponse entityToResponse(ColaboradorContrato entity);

    /* ================= LIST ================= */

    @Mapping(target = "colaboradorNome", source = "colaborador.nome")
    @Mapping(target = "matricula", source = "colaborador.matricula")
    ColaboradorContratoListDTO toListDTO(ColaboradorContrato entity);

    /* ===================== MAPPERS AUXILIARES ===================== */

    @Named("sindicatoToString")
    default String sindicatoToString(Sindicato sindicato) {
        return sindicato != null ? sindicato.getNome() : null;
    }
}
