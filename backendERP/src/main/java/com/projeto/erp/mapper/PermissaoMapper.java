package com.projeto.erp.mapper;

import com.projeto.erp.dtos.PermissaoDTO;
import com.projeto.erp.modelo.Permissao;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PermissaoMapper {

    PermissaoDTO toDTO(Permissao permissao);

    Permissao toEntity(PermissaoDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(PermissaoDTO dto, @MappingTarget Permissao entity);
}



