package com.projeto.erp.mapper;

import com.projeto.erp.dtos.ColaboradorCreateDTOResponse;
import com.projeto.erp.dtos.UsuarioCreateDTORequest;
import com.projeto.erp.dtos.ColaboradorDTOResponse;
import com.projeto.erp.modelo.Colaborador;

import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = { PerfilMapper.class },
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ColaboradorMapper {

    /* ================= CREATE ================= */

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAlteracao", ignore = true)
    @Mapping(target = "matricula", ignore = true)
    @Mapping(target = "onboardingStep", ignore = true)
    @Mapping(target = "emailCorporativo", ignore = true)
    @Mapping(target = "dependentes", ignore = true)
    Colaborador fromCreateDTO(UsuarioCreateDTORequest dto);


    /* ================= RESPONSE ================= */

    @Mapping(target = "usuarioLogin", source = "colaborador.usuario.login")
    @Mapping(target = "usuario", source = "colaborador.usuario")
    @Mapping(target = "emailCorporativo", source = "emailCorporativo")
    @Mapping(target = "senhaTemporaria", source = "senhaTemporaria")
    @Mapping(target = "dataCriacao", source = "colaborador.dataCriacao")
    @Mapping(target = "dataAlteracao", source = "colaborador.dataAlteracao")
    ColaboradorCreateDTOResponse toCreateResponse(Colaborador colaborador, String senhaTemporaria, String emailCorporativo);

    @Mapping(target = "usuarioLogin", source = "colaborador.usuario.login")
    @Mapping(target = "usuario", source = "colaborador.usuario")
    ColaboradorDTOResponse toResponse(Colaborador colaborador);
}
