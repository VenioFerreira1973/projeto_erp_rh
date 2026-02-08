package com.projeto.erp.service;

import com.projeto.erp.dtos.*;
import com.projeto.erp.enumeracoes.OnboardingStep;
import com.projeto.erp.enumeracoes.OnboardingValidacaoStep;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.exception.NegocioException;
import com.projeto.erp.mapper.*;
import com.projeto.erp.modelo.*;
import com.projeto.erp.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class OnboardingValidacoesService {

    private final OnboardingValidacoesRepository validacoesRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final ColaboradorDadosPessoaisRepository dadosRepository;
    private final ColaboradorDependentesRepository dependentesRepository;
    private final ColaboradorEnderecoRepository enderecoRepository;
    private final ColaboradorDadosBancariosRepository dadosBancariosRepository;
    private final ColaboradorDocumentosRepository documentosRepository;
    private final ColaboradorDocumentosAnexosRepository documentosAnexosRepository;

    private final ColaboradorAutenticadoService colaboradorAutenticadoService;
    private final EmailService emailService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;
    private final OnboardingService onboardingService;

    private final ColaboradorDadosPessoaisMapper dadosMapper;
    private final ColaboradorDependentesMapper dependentesMapper;
    private final ColaboradorEnderecoMapper enderecoMapper;
    private final ColaboradorDadosBancariosMapper dadosBancariosMapper;
    private final ColaboradorDocumentosMapper documentosMapper;
    private final ColaboradorDocumentosAnexosMapper documentosAnexosMapper;
    private final OnboardingValidacoesMapper mapper;


    public OnboardingValidacoesService(
            OnboardingValidacoesRepository validacoesRepository,
            ColaboradorRepository colaboradorRepository,
            OnboardingValidacoesMapper mapper,
            ColaboradorAutenticadoService colaboradorAutenticadoService,
            OnboardingService onboardingService, ColaboradorDependentesRepository dependentesRepository,
            EmailService emailService,
            ColaboradorDadosPessoaisRepository dadosRepository,
            ColaboradorEnderecoRepository enderecoRepository,
            ColaboradorDadosBancariosRepository dadosBancariosRepository,
            ColaboradorDocumentosRepository documentosRepository,
            ColaboradorDocumentosAnexosRepository documentosAnexosRepository, OnboardingService onboardingService1,
            ColaboradorDadosPessoaisMapper dadosMapper,
            ColaboradorEnderecoMapper enderecoMapper,
            ColaboradorDadosBancariosMapper dadosBancariosMapper,
            ColaboradorDocumentosMapper documentosMapper,
            ColaboradorDocumentosAnexosMapper documentosAnexosMapper,
            UsuarioAutenticadoService usuarioAutenticadoService, ColaboradorDependentesMapper dependentesMapper
    ) {
        this.validacoesRepository = validacoesRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.mapper = mapper;
        this.colaboradorAutenticadoService = colaboradorAutenticadoService;
        this.dependentesRepository = dependentesRepository;
        this.emailService = emailService;
        this.dadosRepository = dadosRepository;
        this.enderecoRepository = enderecoRepository;
        this.dadosBancariosRepository = dadosBancariosRepository;
        this.documentosRepository = documentosRepository;
        this.documentosAnexosRepository = documentosAnexosRepository;
        this.onboardingService = onboardingService;
        this.dadosMapper = dadosMapper;
        this.enderecoMapper = enderecoMapper;
        this.dadosBancariosMapper = dadosBancariosMapper;
        this.documentosMapper = documentosMapper;
        this.documentosAnexosMapper = documentosAnexosMapper;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
        this.dependentesMapper = dependentesMapper;
    }

    @Transactional(readOnly = true)
    public List<OnboardingValidacoesResponseDTO> listarValidacoes(Long colaboradorId) {
        List<OnboardingValidacoes> validacoes = validacoesRepository.findAllByColaboradorId(colaboradorId);

        if (validacoes.isEmpty()) {
            throw new EntityNotFoundException(
                    "Nenhuma validação anexo encontrada para o colaborador"
            );
        }
        return validacoes.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public OnboardingValidacoesResponseDTO salvarValidacao(OnboardingValidacoesRequestDTO dto) {
        OnboardingValidacoes validacoes = new OnboardingValidacoes();

        validacoes = validacoesRepository.save(
                mapper.toEntity(dto, validacoes)
        );

        return mapper.toResponse(validacoes);
    }

    @Transactional
    public void validarOnboardingLote(
            Long colaboradorId,
            OnboardingValidacoesLoteRequestDTO request,
            Long rhId
    ) {

        Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new NegocioException("Colaborador não encontrado"));

        for (OnboardingValidacaoLoteDTO v : request.validacoes()) {

            OnboardingValidacoes validacao = validacoesRepository
                    .findByColaboradorIdAndOnboardingValidacaoStep(
                            colaboradorId,
                            v.step()
                    )
                    .orElseThrow(() ->
                            new NegocioException("Validação não encontrada para o step " + v.step())
                    );

            if (v.status() == StatusValidacao.REPROVADO) {

                if (v.observacao() == null || v.observacao().isBlank()) {
                    throw new NegocioException("Observação obrigatória ao reprovar");
                }

                validacao.reprovar(v.observacao(), rhId);

            } else {
                validacao.aprovar(rhId);
            }

            validacoesRepository.save(validacao);
        }

        boolean tudoAprovado = validacoesRepository
                .findAllByColaboradorId(colaboradorId)
                .stream()
                .allMatch(v -> v.getStatusValidacao() == StatusValidacao.APROVADO);

        Usuario usuario = colaborador.getUsuario();

        if (tudoAprovado) {
            onboardingService.concluirOnboarding(colaborador);
            emailService.enviarAvisoOnboardingFinalizado(usuario.getEmailPessoal());
        } else {
            emailService.enviarAvisoPendencia(usuario.getEmailPessoal());
        }
    }



    @Transactional(readOnly = true)
    public OnboardingPendenteResponseDTO buscarOnboardingPendente() {
        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        List<OnboardingValidacoes> validacoes =
                validacoesRepository.findAllByColaboradorIdAndStatusValidacao(
                        colaborador.getId(),
                        StatusValidacao.PENDENTE
                );

        Map<OnboardingValidacaoStep, OnboardingValidacoesResponseDTO> map =
                validacoes.stream()
                        .collect(Collectors.toMap(
                                OnboardingValidacoes::getOnboardingValidacaoStep,
                                mapper::toResponse
                        ));

        return new OnboardingPendenteResponseDTO(colaborador.getId(), map);
    }

    @Transactional(readOnly = true)
    public List<OnboardingValidacoesResponseDTO> listarColaboradoresPendentes() {
        return validacoesRepository.findPendentesComNome(StatusValidacao.PENDENTE);
    }

    @Transactional
    private <T> T completarPassoOnboarding(
            OnboardingStep passoAtual,
            OnboardingStep proximoPasso,
            Supplier<T> salvarDadosDoPasso
    ) {
        Usuario usuario = usuarioAutenticadoService.getUsuario();
        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        validarSeEstaEmOnboarding(usuario);

        if (colaborador.getOnboardingStep() != passoAtual) {
            throw new NegocioException("Você não está na etapa de " + passoAtual);
        }

        T dadosSalvos = salvarDadosDoPasso.get();

        colaborador.setOnboardingStep(proximoPasso);

        return dadosSalvos;
    }

    @Transactional
    public ColaboradorDadosPessoaisResponse salvarDadosPessoais(ColaboradorDadosPessoaisRequest request) {
        return completarPassoOnboarding(
                OnboardingStep.DADOS_PESSOAIS,
                OnboardingStep.ENDERECO,
                () -> {
                    Colaborador colaborador = colaboradorAutenticadoService.getColaborador();
                    ColaboradorDadosPessoais dadosPessoais = dadosRepository
                            .findByColaborador(colaborador)
                            .map(existente -> {
                                existente.atualizar(request);
                                return existente;
                            })
                            .orElseGet(() -> dadosMapper.toEntity(request, colaborador));
                    dadosPessoais = dadosRepository.save(dadosPessoais);

                    OnboardingValidacoesRequestDTO dto = new OnboardingValidacoesRequestDTO(colaborador.getId(),
                            OnboardingValidacaoStep.DADOS_PESSOAIS,StatusValidacao.PENDENTE, null);

                    salvarValidacao(dto);

                    return dadosMapper.toResponse(dadosPessoais, null, null);
                }
        );
    }

    @Transactional
    public List<ColaboradorDependentesResponseDTO> salvarDependentes(
            List<ColaboradorDependentesRequestDTO> request
    ) {
        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        if (request == null || request.isEmpty()) {
            return List.of();
        }

        List<ColaboradorDependentes> dependentes = request.stream()
                .map(dto -> dependentesMapper.toEntity(dto, colaborador))
                .toList();

        dependentes = dependentesRepository.saveAll(dependentes);

        OnboardingValidacoesRequestDTO dto = new OnboardingValidacoesRequestDTO(colaborador.getId(),
                OnboardingValidacaoStep.DEPENDENTES,StatusValidacao.PENDENTE, null);

        salvarValidacao(dto);

        return dependentesMapper.toResponseList(dependentes);
    }


    @Transactional
    public ColaboradorEnderecoResponse salvarEndereco(ColaboradorEnderecoRequest request) {
        return completarPassoOnboarding(
                OnboardingStep.ENDERECO,
                OnboardingStep.DADOS_BANCARIOS,
                () -> {
                    Colaborador colaborador = colaboradorAutenticadoService.getColaborador();
                    ColaboradorEndereco endereco = enderecoRepository
                            .findByColaborador(colaborador)
                            .map(existente -> {
                                existente.atualizar(request);
                                return existente;
                            })
                            .orElseGet(() -> enderecoMapper.toEntity(request, colaborador));
                    endereco = enderecoRepository.save(endereco);

                    OnboardingValidacoesRequestDTO dto = new OnboardingValidacoesRequestDTO(colaborador.getId(),
                            OnboardingValidacaoStep.ENDERECO,StatusValidacao.PENDENTE, null);

                    salvarValidacao(dto);

                    return enderecoMapper.toResponse(endereco, null, null);
                }
        );
    }

    @Transactional
    public ColaboradorDadosBancariosResponseDTO salvarDadosBancarios(ColaboradorDadosBancariosRequestDTO request) {
        return completarPassoOnboarding(
                OnboardingStep.DADOS_BANCARIOS,
                OnboardingStep.DOCUMENTOS,
                () -> {
                    Colaborador colaborador = colaboradorAutenticadoService.getColaborador();
                    ColaboradorDadosBancarios dadosBancarios = dadosBancariosRepository
                            .findByColaborador(colaborador)
                            .map(existente -> {
                                existente.atualizar(request);
                                return existente;
                            })
                            .orElseGet(() -> dadosBancariosMapper.toEntity(request, colaborador));
                    dadosBancarios = dadosBancariosRepository.save(dadosBancarios);

                    OnboardingValidacoesRequestDTO dto = new OnboardingValidacoesRequestDTO(colaborador.getId(),
                            OnboardingValidacaoStep.DADOS_BANCARIOS,StatusValidacao.PENDENTE, null);

                    salvarValidacao(dto);
                    return dadosBancariosMapper.toResponse(dadosBancarios,null, null);
                }
        );
    }

    @Transactional
    public ColaboradorDocumentosResponseDTO salvarDocumentos(ColaboradorDocumentosRequestDTO request) {
        return completarPassoOnboarding(
                OnboardingStep.DOCUMENTOS,
                OnboardingStep.DOCUMENTOS_ANEXOS,
                () -> {
                    Colaborador colaborador = colaboradorAutenticadoService.getColaborador();
                    ColaboradorDocumentos documentos = documentosRepository
                            .findByColaborador(colaborador)
                            .map(existente -> {
                                existente.atualizar(request);
                                return existente;
                            })
                            .orElseGet(() -> documentosMapper.toEntity(request, colaborador));
                    documentos = documentosRepository.save(documentos);

                    OnboardingValidacoesRequestDTO dto = new OnboardingValidacoesRequestDTO(colaborador.getId(),
                            OnboardingValidacaoStep.DOCUMENTOS,StatusValidacao.PENDENTE, null);

                    salvarValidacao(dto);

                    return documentosMapper.toResponse(documentos, null, null);
                }
        );
    }

    @Transactional
    public List<ColaboradorDocumentosAnexosResponseDTO> salvarDocumentosAnexos(
            List<ColaboradorDocumentosAnexosRequestDTO> requests
    ) {
        return completarPassoOnboarding(
                OnboardingStep.DOCUMENTOS_ANEXOS,
                OnboardingStep.CONCLUIDO,
                () -> {
                    Colaborador colaborador =
                            colaboradorAutenticadoService.getColaborador();

                    List<ColaboradorDocumentosAnexos> entidades = requests.stream()
                            .map(request -> {
                                ColaboradorDocumentosAnexos entidade =
                                        documentosAnexosRepository
                                                .findByColaboradorAndArquivoUrl(
                                                        colaborador,
                                                        request.arquivoUrl()
                                                )
                                                .map(existente -> {
                                                    existente.atualizar(request);
                                                    return existente;
                                                })
                                                .orElseGet(() ->
                                                        documentosAnexosMapper
                                                                .toEntity(request, colaborador)
                                                );

                                entidade.atualizarDataUpload(Instant.now());

                                return documentosAnexosRepository.save(entidade);
                            })
                            .toList();

                    OnboardingValidacoesRequestDTO dto = new OnboardingValidacoesRequestDTO(colaborador.getId(),
                            OnboardingValidacaoStep.DOCUMENTOS_ANEXOS,StatusValidacao.PENDENTE, null);

                    salvarValidacao(dto);

                    return entidades.stream()
                            .map(documentosAnexosMapper::toResponse)
                            .toList();
                }
        );
    }

    private void validarSeEstaEmOnboarding(Usuario usuario) {
        if (!usuario.isPrimeiroAcesso()) {
            throw new NegocioException("O processo de onboarding já foi concluído anteriormente.");
        }
    }

}

