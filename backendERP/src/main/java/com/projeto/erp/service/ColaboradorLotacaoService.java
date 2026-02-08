package com.projeto.erp.service;

import com.projeto.erp.dtos.*;
import com.projeto.erp.mapper.ColaboradorLotacaoMapper;
import com.projeto.erp.modelo.ColaboradorLotacao;
import com.projeto.erp.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ColaboradorLotacaoService {

    private final ColaboradorLotacaoRepository repository;
    private final CargoRepository cargoRepository;
    private final DepartamentoRepository departamentoRepository;
    private final ColaboradorLotacaoMapper mapper;

    private final ColaboradorLotacaoRepository lotacaoRepository;
    private final ColaboradorRepository colaboradorRepository;

    public ColaboradorLotacaoService(ColaboradorLotacaoRepository repository,
                                     CargoRepository cargoRepository,
                                     DepartamentoRepository departamentoRepository,
                                     ColaboradorLotacaoMapper mapper,
                                     ColaboradorLotacaoRepository lotacaoRepository,
                                     ColaboradorRepository colaboradorRepository) {
        this.repository = repository;
        this.cargoRepository = cargoRepository;
        this.departamentoRepository = departamentoRepository;
        this.mapper = mapper;
        this.lotacaoRepository = lotacaoRepository;
        this.colaboradorRepository = colaboradorRepository;
    }


    @Transactional(readOnly = true)
    public ColaboradorLotacaoDTOResponse obter(Long colaboradorId) {
        ColaboradorLotacao contrato = repository.findByIdComRelacionamentos(colaboradorId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Contrato de funcionário não encontrado"));

        return mapper.toResponse(contrato);
    }

    @Transactional
    public ColaboradorLotacaoDTOResponse criar(ColaboradorLotacaoCreateDTO dto) {

        ColaboradorLotacao lotacao = mapper.toEntity(dto);

        lotacao.alterarColaborador(
                colaboradorRepository.getReferenceById(dto.colaboradorId())
        );
        lotacao.alterarCargo(
                cargoRepository.getReferenceById(dto.cargoId())
        );

        if (dto.departamentoId() != null) {
            lotacao.alterarDepartamento(
                    departamentoRepository.getReferenceById(dto.departamentoId())
            );
        }

        lotacao = lotacaoRepository.save(lotacao);

        return mapper.toResponse(lotacao);
    }


    @Transactional
    public ColaboradorLotacaoDTOResponse atualizar(Long id, ColaboradorLotacaoUpdateDTO dto) {

        ColaboradorLotacao lotacao = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Contrato funcionário não encontrado"));

        mapper.updateFromUpdateDTO(dto, lotacao);

        if (dto.cargoId() != null) {
            lotacao.alterarCargo(
                    cargoRepository.getReferenceById(dto.cargoId())
            );
        }

        if (dto.departamentoId() != null) {
            lotacao.alterarDepartamento(
                    departamentoRepository.getReferenceById(dto.departamentoId())
            );
        }

        if (dto.gestorId() != null) {
            lotacao.altearGestor(
                    colaboradorRepository.getReferenceById(dto.gestorId())
            );
        }

        return mapper.toResponse(lotacao);
    }

}
