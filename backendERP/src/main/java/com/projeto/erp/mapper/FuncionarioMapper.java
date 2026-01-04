package com.projeto.erp.mapper;

import com.projeto.erp.dtos.FuncionarioDTORequest;
import com.projeto.erp.dtos.FuncionarioDTOResponse;
import com.projeto.erp.modelo.Funcionario;

import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = { CargoMapper.class, DepartamentoMapper.class }
)
public interface FuncionarioMapper {


    @Mapping(target = "usuarioLogin", source = "usuario.login")
    @Mapping(target = "cargoDTO", source = "cargo")
    @Mapping(target = "departamentoDTO", source = "departamento")
    FuncionarioDTOResponse toResponse(Funcionario funcionario);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "cargo", ignore = true)
    @Mapping(target = "departamento", ignore = true)
    Funcionario toEntity(FuncionarioDTORequest dto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "cargo", ignore = true)
    @Mapping(target = "departamento", ignore = true)
    void updateEntityFromDTO(FuncionarioDTORequest dto, @MappingTarget Funcionario entity);
}

