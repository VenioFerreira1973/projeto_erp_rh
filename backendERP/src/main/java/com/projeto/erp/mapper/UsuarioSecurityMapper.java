package com.projeto.erp.mapper;

import com.projeto.erp.dtos.PermissaoDTO;
import com.projeto.erp.dtos.UsuarioSecurityDTO;
import com.projeto.erp.dtos.UsuarioSecurityResponse;
import com.projeto.erp.modelo.Perfil;
import com.projeto.erp.modelo.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioSecurityMapper {

    @Mapping(target = "permissoes", source = "perfis")
    UsuarioSecurityDTO toSecurityDTO(Usuario usuario);

    @Mapping(target = "permissoes", source = "perfis")
    UsuarioSecurityResponse toSecurityResponse(Usuario usuario);

    default List<PermissaoDTO> mapPerfisToPermissoes(List<Perfil> perfis) {
        return perfis.stream()
                .flatMap(p -> p.getPermissoes().stream())
                .map(per -> new PermissaoDTO(per.getId(), per.getDescricao()))
                .toList();
    }
}
