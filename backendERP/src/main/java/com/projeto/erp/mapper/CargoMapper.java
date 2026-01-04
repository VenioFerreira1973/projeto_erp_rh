package com.projeto.erp.mapper;

import com.projeto.erp.dtos.CargoDTO;
import com.projeto.erp.modelo.Cargo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CargoMapper {

    CargoDTO toDTO(Cargo cargo);

    Cargo toEntity(CargoDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(CargoDTO dto, @MappingTarget Cargo entity);
}



