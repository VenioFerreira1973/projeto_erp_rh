package com.projeto.erp.service;

import com.projeto.erp.dtos.ColaboradorDocumentosRequestDTO;
import com.projeto.erp.dtos.ColaboradorDocumentosResponseDTO;
import com.projeto.erp.enumeracoes.OnboardingValidacaoStep;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.mapper.ColaboradorDocumentosMapper;
import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorDocumentos;
import com.projeto.erp.modelo.OnboardingValidacoes;
import com.projeto.erp.repository.ColaboradorDocumentosRepository;
import com.projeto.erp.repository.ColaboradorRepository;
import com.projeto.erp.repository.OnboardingValidacoesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ColaboradorDocumentosService {

    private final ColaboradorDocumentosRepository repository;
    private final ColaboradorRepository colaboradorRepository;
    private final ColaboradorDocumentosMapper mapper;
    private final OnboardingValidacoesRepository onboardingValidacoesRepository;
    private final ColaboradorAutenticadoService colaboradorAutenticadoService;

    public ColaboradorDocumentosService(
            ColaboradorDocumentosRepository repository,
            ColaboradorRepository colaboradorRepository,
            ColaboradorDocumentosMapper mapper, OnboardingValidacoesRepository onboardingValidacoesRepository, ColaboradorAutenticadoService colaboradorAutenticadoService
    ) {
        this.repository = repository;
        this.colaboradorRepository = colaboradorRepository;
        this.mapper = mapper;
        this.onboardingValidacoesRepository = onboardingValidacoesRepository;
        this.colaboradorAutenticadoService = colaboradorAutenticadoService;
    }

    @Transactional(readOnly = true)
    public ColaboradorDocumentosResponseDTO obterPorColaborador(Long colaboradorId) {

        Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Funcionário não encontrado com id: " + colaboradorId
                ));

        ColaboradorDocumentos documentos = repository.findByColaborador(colaborador)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Documentos não encontrados para o colaborador"
                ));

        OnboardingValidacoes validacoes = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(colaborador.getId(),
                        OnboardingValidacaoStep.DOCUMENTOS).orElse(null);

        String observacao = validacoes != null ? validacoes.getObservacao() : null;

        StatusValidacao statusValidacao = validacoes != null ? validacoes.getStatusValidacao() : null;


        return mapper.toResponse(documentos, observacao, statusValidacao );
    }

    @Transactional
    public ColaboradorDocumentosResponseDTO cadastrar(
            Long colaboradorId,
            ColaboradorDocumentosRequestDTO request
    ) {
        Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Funcionário não encontrado com id: " + colaboradorId
                ));

        ColaboradorDocumentos documentos = repository.findByColaborador(colaborador)
                .map(existing -> {
                    mapper.updateEntity(request, existing);
                    return existing;
                })
                .orElseGet(() -> mapper.toEntity(request, colaborador));

        return mapper.toResponse(repository.save(documentos), null, null);
    }

    @Transactional
    public ColaboradorDocumentosResponseDTO corrigir(
            ColaboradorDocumentosRequestDTO request
    ) {

        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        OnboardingValidacoes validacao = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(
                        colaborador.getId(),
                        OnboardingValidacaoStep.DOCUMENTOS
                )
                .orElseThrow(() -> new IllegalStateException(
                        "Validação de documentos não encontrada"
                ));

        if (validacao.getStatusValidacao() != StatusValidacao.REPROVADO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Correção permitida apenas quando status = REPROVADO"
            );
        }

        ColaboradorDocumentos documentos = repository
                .findByColaborador(colaborador)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Documentos não encontrados"
                ));

        mapper.updateEntity(request, documentos);
        repository.save(documentos);

        validacao.alterarStatus(StatusValidacao.PENDENTE);
        validacao.atualizarObservacao(null);
        onboardingValidacoesRepository.save(validacao);

        return mapper.toResponse(
                documentos,
                null,
                StatusValidacao.PENDENTE
        );
    }

}
