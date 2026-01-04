package com.projeto.erp.mapper;

import com.projeto.erp.dtos.DepartamentoDTO;
import com.projeto.erp.modelo.Departamento;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface DepartamentoMapper {

    DepartamentoDTO toDTO(Departamento departamento);

    Departamento toEntity(DepartamentoDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(DepartamentoDTO dto, @MappingTarget Departamento entity);
}
