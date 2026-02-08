package com.projeto.erp.service;

import com.projeto.erp.dtos.ColaboradorDadosBancariosRequestDTO;
import com.projeto.erp.dtos.ColaboradorDadosBancariosResponseDTO;
import com.projeto.erp.enumeracoes.OnboardingValidacaoStep;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.mapper.ColaboradorDadosBancariosMapper;
import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorDadosBancarios;
import com.projeto.erp.modelo.OnboardingValidacoes;
import com.projeto.erp.repository.ColaboradorDadosBancariosRepository;
import com.projeto.erp.repository.ColaboradorRepository;
import com.projeto.erp.repository.OnboardingValidacoesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ColaboradorDadosBancariosService {

    private final ColaboradorDadosBancariosRepository repository;
    private final ColaboradorRepository colaboradorRepository;
    private final ColaboradorDadosBancariosMapper mapper;
    private final OnboardingValidacoesRepository onboardingValidacoesRepository;
    private final ColaboradorAutenticadoService colaboradorAutenticadoService;

    public ColaboradorDadosBancariosService(
            ColaboradorDadosBancariosRepository repository,
            ColaboradorRepository colaboradorRepository,
            ColaboradorDadosBancariosMapper mapper, OnboardingValidacoesRepository onboardingValidacoesRepository, ColaboradorAutenticadoService colaboradorAutenticadoService
    ) {
        this.repository = repository;
        this.colaboradorRepository = colaboradorRepository;
        this.mapper = mapper;
        this.onboardingValidacoesRepository = onboardingValidacoesRepository;
        this.colaboradorAutenticadoService = colaboradorAutenticadoService;
    }

    @Transactional(readOnly = true)
    public ColaboradorDadosBancariosResponseDTO obterPorColaborador(Long colaboradorId) {

        Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Colaborador não encontrado com id: " + colaboradorId
                ));

        ColaboradorDadosBancarios dadosBancarios = repository.findByColaborador(colaborador)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Dados bancários não encontrados para o colaborador"
                ));

        OnboardingValidacoes validacoes = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(colaborador.getId(),
                        OnboardingValidacaoStep.DADOS_BANCARIOS).orElse(null);

        StatusValidacao statusValidacao = validacoes != null ? validacoes.getStatusValidacao() : null;

        String observacao = validacoes != null ? validacoes.getObservacao() : null;

        return mapper.toResponse(dadosBancarios, observacao, statusValidacao);
    }

    @Transactional
    public ColaboradorDadosBancariosResponseDTO cadastrar(
            Long colaboradorId,
            ColaboradorDadosBancariosRequestDTO request
    ) {
        Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Funcionário não encontrado com id: " + colaboradorId
                ));

        ColaboradorDadosBancarios dadosBancarios = repository.findByColaborador(colaborador)
                .map(existing -> {
                    mapper.updateEntity(request, existing);
                    return existing;
                })
                .orElseGet(() -> mapper.toEntity(request, colaborador));

        return mapper.toResponse(repository.save(dadosBancarios), null, null);
    }

    @Transactional
    public ColaboradorDadosBancariosResponseDTO corrigir(
            ColaboradorDadosBancariosRequestDTO request
    ) {

        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        OnboardingValidacoes validacao = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(
                        colaborador.getId(),
                        OnboardingValidacaoStep.DADOS_BANCARIOS
                )
                .orElseThrow(() -> new IllegalStateException(
                        "Validação de dados bancários não encontrada"
                ));

        if (validacao.getStatusValidacao() != StatusValidacao.REPROVADO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Correção permitida apenas quando status = REPROVADO"
            );
        }

        ColaboradorDadosBancarios dadosBancarios = repository
                .findByColaborador(colaborador)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Dados bancários não encontrados"
                ));

        mapper.updateEntity(request, dadosBancarios);
        repository.save(dadosBancarios);

        validacao.alterarStatus(StatusValidacao.PENDENTE);
        validacao.atualizarObservacao(null);
        onboardingValidacoesRepository.save(validacao);

        return mapper.toResponse(
                dadosBancarios,
                null,
                StatusValidacao.PENDENTE
        );
    }

}
