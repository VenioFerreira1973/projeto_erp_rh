package com.projeto.erp.service;

import com.projeto.erp.dtos.*;
import com.projeto.erp.enumeracoes.OnboardingValidacaoStep;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.exception.NegocioException;
import com.projeto.erp.mapper.ColaboradorDependentesMapper;
import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorDependentes;
import com.projeto.erp.modelo.OnboardingValidacoes;
import com.projeto.erp.repository.ColaboradorDependentesRepository;
import com.projeto.erp.repository.ColaboradorRepository;
import com.projeto.erp.repository.OnboardingValidacoesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ColaboradorDependentesService {

    private final ColaboradorDependentesRepository repository;
    private final ColaboradorDependentesMapper mapper;
    private final ColaboradorRepository colaboradorRepository;
    private final OnboardingValidacoesService onboardingValidacoeeService;
    private final OnboardingValidacoesRepository onboardingValidacoesRepository;
    private final ColaboradorAutenticadoService colaboradorAutenticadoService;

    public ColaboradorDependentesService(
            ColaboradorDependentesRepository repository,
            ColaboradorDependentesMapper mapper,
            ColaboradorAutenticadoService colaboradorAutenticadoService, ColaboradorRepository colaboradorRepository, OnboardingValidacoesService onboardingValidacoeeService, OnboardingValidacoesRepository onboardingValidacoesRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.colaboradorAutenticadoService = colaboradorAutenticadoService;
        this.colaboradorRepository = colaboradorRepository;
        this.onboardingValidacoeeService = onboardingValidacoeeService;
        this.onboardingValidacoesRepository = onboardingValidacoesRepository;
    }

    @Transactional(readOnly = true)
    public DependentesComObservacaoResponseDTO obterPorColaborador(Long colaboradorId) {

        Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Colaborador não encontrado com id: " + colaboradorId)
                );

        List<ColaboradorDependentes> dependentes =
                repository.findByColaborador(colaborador);

        if (dependentes.isEmpty()) {
            throw new EntityNotFoundException(
                    "Dependentes não encontrados para o colaborador"
            );
        }

        OnboardingValidacoes validacoes = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(colaborador.getId(),
                        OnboardingValidacaoStep.DEPENDENTES).orElse(null);

        String observacao = validacoes != null ? validacoes.getObservacao() : null;

        StatusValidacao statusValidacao = validacoes != null ? validacoes.getStatusValidacao() : null;

        List<ColaboradorDependentesResponseDTO> dependentesDto =
                dependentes.stream()
                        .map(mapper::toResponse)
                        .toList();

        return new DependentesComObservacaoResponseDTO(observacao, statusValidacao, dependentesDto);
    }

    @Transactional(readOnly = true)
    public DependentesComObservacaoResponseDTO listarDependentes() {

        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        OnboardingValidacoes validacoes =
                onboardingValidacoesRepository.findByColaboradorId(colaborador.getId());

        String observacao = validacoes != null
                ? validacoes.getObservacao()
                : null;

        StatusValidacao statusValidacao = validacoes != null ? validacoes.getStatusValidacao() : null;

        List<ColaboradorDependentesResponseDTO> dependentes = repository
                .findAllByColaborador(colaborador)
                .stream()
                .map(mapper::toResponse) .toList();

        return new DependentesComObservacaoResponseDTO(observacao, statusValidacao, dependentes);
    }

    @Transactional
    public List<ColaboradorDependentesResponseDTO> adicionarDependentes(
            List<ColaboradorDependentesRequestDTO> requests
    ) {
        Colaborador colaborador =
                colaboradorAutenticadoService.getColaborador();

        OnboardingValidacoesRequestDTO dto = new OnboardingValidacoesRequestDTO(colaborador.getId(),
                OnboardingValidacaoStep.DEPENDENTES, StatusValidacao.PENDENTE, null);

        onboardingValidacoeeService.salvarValidacao(dto);

        return requests.stream()
                .map(req -> {
                    ColaboradorDependentes dep =
                            mapper.toEntity(req, colaborador);
                    return mapper.toResponse(repository.save(dep));
                })
                .toList();
    }

    @Transactional
    public ColaboradorDependentesResponseDTO atualizarDependente(
            Long id,
            ColaboradorDependentesRequestDTO request
    ) {
        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        ColaboradorDependentes dependente = repository
                .findByIdAndColaborador(id, colaborador)
                .orElseThrow(() -> new NegocioException("Dependente não encontrado"));

        mapper.updateEntity(request, dependente);
        return mapper.toResponse(dependente);
    }

    @Transactional
    public void removerDependente(Long id) {
        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        ColaboradorDependentes dependente = repository
                .findByIdAndColaborador(id, colaborador)
                .orElseThrow(() -> new NegocioException("Dependente não encontrado"));

        dependente.desativar();
    }

    @Transactional
    public List<ColaboradorDependentesResponseDTO> corrigir(
            List<ColaboradorDependentesRequestDTO> request
    ) {

        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        OnboardingValidacoes validacao = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(
                        colaborador.getId(),
                        OnboardingValidacaoStep.DEPENDENTES
                )
                .orElseThrow(() -> new IllegalStateException(
                        "Validação de dependentes não encontrada."
                ));

        if (validacao.getStatusValidacao() != StatusValidacao.REPROVADO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Correção permitida apenas quando status = REPROVADO"
            );
        }

        List<ColaboradorDependentes> dependentes =
                repository.findByColaborador(colaborador);

        if (dependentes.isEmpty()) {
            throw new EntityNotFoundException("Dados pessoais não encontrados");
        }

        Map<Long, ColaboradorDependentes> dependentesPorId =
                dependentes.stream()
                        .collect(Collectors.toMap(
                                ColaboradorDependentes::getId,
                                Function.identity()
                        ));

        for (ColaboradorDependentesRequestDTO dto : request) {
            ColaboradorDependentes entity = dependentesPorId.get(dto.id());

            if (entity == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Dependente inválido ou não pertence ao colaborador"
                );
            }

            mapper.updateEntity(dto, entity);
        }

        repository.saveAll(dependentes);

        validacao.alterarStatus(StatusValidacao.PENDENTE);
        validacao.atualizarObservacao(null);
        onboardingValidacoesRepository.save(validacao);

        return mapper.toResponseList(dependentes);
    }

}
