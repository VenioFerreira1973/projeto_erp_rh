package com.projeto.erp.service;

import com.projeto.erp.dtos.*;
import com.projeto.erp.enumeracoes.OnboardingValidacaoStep;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.mapper.ColaboradorDadosPessoaisMapper;
import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorDadosPessoais;
import com.projeto.erp.modelo.OnboardingValidacoes;
import com.projeto.erp.repository.ColaboradorDadosPessoaisRepository;
import com.projeto.erp.repository.ColaboradorRepository;
import com.projeto.erp.repository.OnboardingValidacoesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ColaboradorDadosPessoaisService {

    private final ColaboradorDadosPessoaisRepository repository;
    private final ColaboradorRepository colaboradorRepository;
    private final ColaboradorDadosPessoaisMapper mapper;
    private final OnboardingValidacoesRepository onboardingValidacoesRepository;
    private final ColaboradorAutenticadoService colaboradorAutenticadoService;

    public ColaboradorDadosPessoaisService(ColaboradorDadosPessoaisRepository repository, ColaboradorRepository colaboradorRepository, ColaboradorDadosPessoaisMapper mapper, OnboardingValidacoesRepository onboardingValidacoesRepository, ColaboradorAutenticadoService colaboradorAutenticadoService) {
        this.repository = repository;
        this.colaboradorRepository = colaboradorRepository;
        this.mapper = mapper;
        this.onboardingValidacoesRepository = onboardingValidacoesRepository;
        this.colaboradorAutenticadoService = colaboradorAutenticadoService;
    }


    @Transactional(readOnly = true)
    public ColaboradorDadosPessoaisResponse obterPorColaborador(Long colaboradorId) {

        Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Funcionário não encontrado com id: " + colaboradorId
                ));

        ColaboradorDadosPessoais dadosPessoais = repository.findByColaborador(colaborador)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Dados pessoais não encontrados para o funcionário"
                ));

        OnboardingValidacoes validacoes = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(colaborador.getId(),
                        OnboardingValidacaoStep.DADOS_PESSOAIS).orElse(null);

        String observacao = validacoes != null ? validacoes.getObservacao() : null;

        StatusValidacao statusValidacao = validacoes != null ? validacoes.getStatusValidacao() : null;

        return mapper.toResponse(dadosPessoais, observacao, statusValidacao);
    }


    @Transactional
    public ColaboradorDadosPessoaisResponse cadastrar(
            Long colaboradorId,
            ColaboradorDadosPessoaisRequest request
    ) {
        Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Funcionário não encontrado com id: " + colaboradorId
                ));

        ColaboradorDadosPessoais dadosPessoais = repository.findByColaborador(colaborador)
                .map(existing -> {
                    mapper.updateEntity(request, existing);
                    return existing;
                })
                .orElseGet(() -> mapper.toEntity(request, colaborador));

        return mapper.toResponse(repository.save(dadosPessoais), null, null);
    }

    @Transactional
    public ColaboradorDadosPessoaisResponse corrigir(ColaboradorDadosPessoaisRequest request) {

        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        OnboardingValidacoes validacao = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(
                        colaborador.getId(),
                        OnboardingValidacaoStep.DADOS_PESSOAIS
                )
                .orElseThrow(() -> new IllegalStateException(
                        "Validação de dados pessoais não encontrada"
                ));


        if (validacao.getStatusValidacao() != StatusValidacao.REPROVADO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Correção permitida apenas quando status = REPROVADO"
            );
        }

        ColaboradorDadosPessoais dados = repository.findByColaborador(colaborador)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Dados pessoais não encontrados"
                ));

        mapper.updateEntity(request, dados);
        repository.save(dados);

        validacao.alterarStatus(StatusValidacao.PENDENTE);
        validacao.atualizarObservacao(null);
        onboardingValidacoesRepository.save(validacao);

        return mapper.toResponse(dados, null, StatusValidacao.PENDENTE);
    }

}
