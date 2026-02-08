package com.projeto.erp.mapper;

import com.projeto.erp.dtos.*;
import com.projeto.erp.modelo.*;
import com.projeto.erp.repository.CentroCustoRepository;
import org.mapstruct.*;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = { CentroCustoRepository.class })
public interface ColaboradorLotacaoMapper {

    /* ===================== RESPONSE ===================== */

    @Mapping(target = "colaboradorId", source = "colaborador.id")
    @Mapping(target = "colaboradorNome", source = "colaborador.nome")
    @Mapping(target = "matricula", source = "colaborador.matricula")
    @Mapping(target = "cargo", source = "cargo", qualifiedByName = "cargoToString")
    @Mapping(target = "departamento", source = "departamento", qualifiedByName = "departamentoToString")
    @Mapping(target = "gestor", source = "gestor", qualifiedByName = "colaboradorToString")
    @Mapping(target = "centroCusto", source = "centroCusto", qualifiedByName = "centroCustoToString")
    ColaboradorLotacaoDTOResponse toResponse(ColaboradorLotacao entity);


    /* ===================== REQUEST → ENTITY ===================== */

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)

    // Relacionamentos resolvidos no service
    @Mapping(target = "colaborador", ignore = true)
    @Mapping(target = "cargo", ignore = true)
    @Mapping(target = "departamento", ignore = true)
    @Mapping(target = "gestor", ignore = true)
    @Mapping(target = "centroCusto", ignore = true)
    ColaboradorLotacao toEntity(ColaboradorLotacaoCreateDTO dto);


    /* ===================== UPDATE ===================== */

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "colaborador", ignore = true)
    @Mapping(target = "cargo", ignore = true)
    @Mapping(target = "departamento", ignore = true)
    @Mapping(target = "gestor", ignore = true)
    @Mapping(target = "centroCusto", ignore = true)

    void updateEntityFromDTO(
            ColaboradorLotacaoDTORequest dto,
            @MappingTarget ColaboradorLotacao entity
    );


    /* ================= CREATE ================= */

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "colaborador", ignore = true)
    @Mapping(target = "cargo", ignore = true)
    @Mapping(target = "departamento", ignore = true)
    @Mapping(target = "gestor", ignore = true)
    @Mapping(target = "centroCusto", source = "centroCustoId", qualifiedByName = "mapCentroCusto")
    ColaboradorLotacao fromCreateDTO(ColaboradorLotacaoCreateDTO dto);

    /* ================= UPDATE ================= */

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "cargo", ignore = true)
    @Mapping(target = "departamento", ignore = true)
    @Mapping(target = "gestor", ignore = true)
    void updateFromUpdateDTO(
            ColaboradorLotacaoUpdateDTO dto,
            @MappingTarget ColaboradorLotacao entity
    );

    /* ================= RESPONSE ================= */

    @Mapping(target = "colaboradorId", source = "colaborador.id")
    @Mapping(target = "colaboradorNome", source = "colaborador.nome")
    @Mapping(target = "matricula", source = "colaborador.matricula")
    @Mapping(target = "cargo", source = "cargo.nome")
    @Mapping(target = "departamento", source = "departamento.descricao")
    @Mapping(target = "gestor", source = "gestor.nome")
    @Mapping(target = "centroCusto", source = "centroCusto.nome")
    ColaboradorLotacaoDTOResponse entityToResponse(ColaboradorLotacao entity);

    /* ================= LIST ================= */

    @Mapping(target = "colaboradorNome", source = "colaborador.nome")
    @Mapping(target = "matricula", source = "colaborador.matricula")
    @Mapping(target = "cargo", source = "cargo.nome")
    @Mapping(target = "departamento", source = "departamento.descricao")
    ColaboradorLotacaoListDTO toListDTO(ColaboradorLotacao entity);

    /* ===================== MAPPERS AUXILIARES ===================== */

    @Named("cargoToString")
    default String cargoToString(Cargo cargo) {
        return cargo != null ? cargo.getNome() : null;
    }

    @Named("departamentoToString")
    default String departamentoToString(Departamento departamento) {
        return departamento != null ? departamento.getDescricao() : null;
    }

    @Named("colaboradorToString")
    default String colaboradorToString(Colaborador colaborador) {
        return colaborador != null ? colaborador.getNome() : null;
    }

    @Named("centroCustoToString")
    default String centroCustoToString(CentroCusto centroCusto) {
        return centroCusto != null ? centroCusto.getDescricao() : null;
    }

    @Named("mapCentroCusto")
    default CentroCusto mapCentroCusto(Long id) {
        if (id == null) return null;
        return new CentroCusto(id);
    }

}
