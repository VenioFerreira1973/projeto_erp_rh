package com.projeto.erp.mapper;

import com.projeto.erp.dtos.CargoDTO;
import com.projeto.erp.mapper.config.CentralMapperConfig;
import com.projeto.erp.modelo.Cargo;
import org.mapstruct.*;

@Mapper(componentModel = "spring", config = CentralMapperConfig.class)
public interface CargoMapper {

    CargoDTO toDTO(Cargo cargo);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "descricao", source = "descricao")
    Cargo toEntity(CargoDTO dto);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            ignoreByDefault = true
    )
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "descricao", source = "descricao")
    void updateEntityFromDTO(CargoDTO dto, @MappingTarget Cargo entity);
}



