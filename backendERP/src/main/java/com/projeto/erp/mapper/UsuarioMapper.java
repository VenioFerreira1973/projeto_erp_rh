package com.projeto.erp.mapper;

import com.projeto.erp.dtos.UsuarioDTORequest;
import com.projeto.erp.dtos.UsuarioDTOResponse;
import com.projeto.erp.modelo.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTOResponse toDTO(Usuario usuario);

    Usuario toEntity(UsuarioDTORequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UsuarioDTORequest dto, @MappingTarget Usuario entity);
}
