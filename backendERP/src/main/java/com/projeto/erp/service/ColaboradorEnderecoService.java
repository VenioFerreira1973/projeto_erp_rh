package com.projeto.erp.service;

import com.projeto.erp.dtos.ColaboradorEnderecoRequest;
import com.projeto.erp.dtos.ColaboradorEnderecoResponse;
import com.projeto.erp.enumeracoes.OnboardingValidacaoStep;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.mapper.ColaboradorEnderecoMapper;
import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorEndereco;
import com.projeto.erp.modelo.OnboardingValidacoes;
import com.projeto.erp.repository.ColaboradorEnderecoRepository;
import com.projeto.erp.repository.ColaboradorRepository;
import com.projeto.erp.repository.OnboardingValidacoesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ColaboradorEnderecoService {

    private final ColaboradorEnderecoRepository repository;
    private final ColaboradorRepository colaboradorRepository;
    private final ColaboradorEnderecoMapper mapper;
    private final OnboardingValidacoesRepository onboardingValidacoesRepository;
    private final ColaboradorAutenticadoService colaboradorAutenticadoService;

    public ColaboradorEnderecoService(ColaboradorEnderecoRepository repository,
                                      ColaboradorRepository colaboradorRepository,
                                      ColaboradorEnderecoMapper mapper, OnboardingValidacoesRepository onboardingValidacoesRepository, ColaboradorAutenticadoService colaboradorAutenticadoService) {
        this.repository = repository;
        this.colaboradorRepository = colaboradorRepository;
        this.mapper = mapper;
        this.onboardingValidacoesRepository = onboardingValidacoesRepository;
        this.colaboradorAutenticadoService = colaboradorAutenticadoService;
    }


    @Transactional(readOnly = true)
    public ColaboradorEnderecoResponse obterPorColaborador(Long colaboradorId) {

        Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Colaborador não encontrado com id: " + colaboradorId
                ));

        ColaboradorEndereco endereco = repository.findByColaborador(colaborador)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Endereço não encontrados para o colaborador"
                ));

        OnboardingValidacoes validacoes = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(colaborador.getId(),
                        OnboardingValidacaoStep.ENDERECO).orElse(null);

        String observacao = validacoes != null ? validacoes.getObservacao() : null;

        StatusValidacao statusValidacao = validacoes != null ? validacoes.getStatusValidacao() : null;

        return mapper.toResponse(endereco, observacao, statusValidacao);
    }


    @Transactional
    public ColaboradorEnderecoResponse cadastrar(
            Long colaboradorId,
            ColaboradorEnderecoRequest request
    ) {
        Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Funcionário não encontrado com id: " + colaboradorId
                ));

        ColaboradorEndereco endereco = repository.findByColaborador(colaborador)
                .map(existing -> {
                    mapper.updateEntity(request, existing);
                    return existing;
                })
                .orElseGet(() -> mapper.toEntity(request, colaborador));

        return mapper.toResponse(repository.save(endereco), null, null);
    }

    @Transactional
    public ColaboradorEnderecoResponse corrigir(ColaboradorEnderecoRequest request) {

        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        OnboardingValidacoes validacao = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(
                        colaborador.getId(),
                        OnboardingValidacaoStep.ENDERECO
                )
                .orElseThrow(() -> new IllegalStateException(
                        "Validação de endereço não encontrada"
                ));

        if (validacao.getStatusValidacao() != StatusValidacao.REPROVADO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Correção permitida apenas quando status = REPROVADO"
            );
        }

        ColaboradorEndereco endereco = repository
                .findByColaborador(colaborador)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Endereço não encontrado"
                ));

        mapper.updateEntity(request, endereco);
        repository.save(endereco);

        validacao.alterarStatus(StatusValidacao.PENDENTE);
        validacao.atualizarObservacao(null);
        onboardingValidacoesRepository.save(validacao);

        return mapper.toResponse(endereco, null, StatusValidacao.PENDENTE);
    }


}
