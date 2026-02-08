package com.projeto.erp.service;

import com.projeto.erp.dtos.*;
import com.projeto.erp.mapper.ColaboradorContratoMapper;
import com.projeto.erp.modelo.*;
import com.projeto.erp.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColaboradorContratoService {

    private final ColaboradorContratoRepository repository;
    private final ColaboradorContratoMapper mapper;

    private final ColaboradorContratoRepository contratoRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final EmpresaRepository empresaRepository;


    public ColaboradorContratoService(ColaboradorContratoRepository repository,
                                      ColaboradorContratoMapper mapper,
                                      ColaboradorContratoRepository contratoRepository,
                                      ColaboradorRepository colaboradorRepository,
                                      EmpresaRepository empresaRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.contratoRepository = contratoRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.empresaRepository = empresaRepository;
    }


    @Transactional(readOnly = true)
    public ColaboradorContratoDTOResponse obter(Long colaboradorId) {
        ColaboradorContrato contrato = repository.findByIdComRelacionamentos(colaboradorId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Contrato de funcionário não encontrado"));

        return mapper.toResponse(contrato);
    }

    @Transactional
    public ColaboradorContratoDTOResponse criar(ColaboradorContratoCreateDTO dto) {

        ColaboradorContrato contrato = mapper.toEntity(dto);

        contrato.alterarColaborador(
                colaboradorRepository.getReferenceById(dto.colaboradorId())
        );
        contrato.alterarEmpresa(
                empresaRepository.getReferenceById(dto.empresaId())
        );

        contrato = contratoRepository.save(contrato);

        return mapper.toResponse(contrato);
    }


    @Transactional
    public ColaboradorContratoDTOResponse atualizar(Long id, ColaboradorContratoUpdateDTO dto) {

        ColaboradorContrato contrato = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Contrato funcionário não encontrado"));

        mapper.updateFromUpdateDTO(dto, contrato);

        return mapper.toResponse(contrato);
    }

}
