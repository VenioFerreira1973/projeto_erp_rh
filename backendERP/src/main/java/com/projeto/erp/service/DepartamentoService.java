package com.projeto.erp.service;

import com.projeto.erp.dtos.DepartamentoDTO;
import com.projeto.erp.mapper.DepartamentoMapper;
import com.projeto.erp.modelo.Departamento;
import com.projeto.erp.repository.DepartamentoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartamentoService {

    private final DepartamentoRepository repository;
    private final DepartamentoMapper mapper;

    public DepartamentoService(DepartamentoRepository repository, DepartamentoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<DepartamentoDTO> listar(){
        List<Departamento> departamentos = repository.findAll();

        List<DepartamentoDTO> dtos = new ArrayList<>();
        for (Departamento departamento : departamentos) {
            dtos.add(mapper.toDTO(departamento));
        }

        return dtos;
    }

}
