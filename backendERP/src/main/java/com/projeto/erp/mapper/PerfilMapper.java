package com.projeto.erp.mapper;

import com.projeto.erp.dtos.PerfilDTO;
import com.projeto.erp.modelo.Perfil;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PermissaoMapper.class})
public interface PerfilMapper {

    @Mapping(target = "permissoes", source = "permissoes")
    @Mapping(
            target = "ativo",
            expression = "java(perfil.getStatus() == com.projeto.erp.enumeracoes.Status.ATIVO)"
    )
    PerfilDTO toDTO(Perfil perfil);

    @Mapping(target = "permissoes", source = "permissoes")
    Perfil toEntity(PerfilDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(PerfilDTO dto, @MappingTarget Perfil entity);
}


