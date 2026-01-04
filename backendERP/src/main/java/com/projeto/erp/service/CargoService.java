package com.projeto.erp.service;

import com.projeto.erp.dtos.CargoDTO;
import com.projeto.erp.mapper.CargoMapper;
import com.projeto.erp.modelo.Cargo;
import com.projeto.erp.repository.CargoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CargoService {

    private final CargoRepository repository;
    private final CargoMapper mapper;

    public CargoService(CargoRepository repository, CargoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CargoDTO> listar(){
        List<Cargo> cargos = repository.findAll();

        List<CargoDTO> dtos = new ArrayList<>();
        for (Cargo cargo : cargos) {
            dtos.add(mapper.toDTO(cargo));
        }

        return dtos;
    }

}
