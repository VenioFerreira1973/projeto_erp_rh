package com.projeto.erp.service;

import com.projeto.erp.dtos.*;
import com.projeto.erp.enumeracoes.OnboardingStep;
import com.projeto.erp.enumeracoes.OnboardingValidacaoStep;
import com.projeto.erp.enumeracoes.Status;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.exception.NegocioException;
import com.projeto.erp.mapper.*;
import com.projeto.erp.mapper.ColaboradorEnderecoMapper;
import com.projeto.erp.modelo.*;
import com.projeto.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class OnboardingService {

    private final ColaboradorDadosPessoaisRepository dadosRepository;
    private final ColaboradorEnderecoRepository enderecoRepository;
    private final ColaboradorDadosBancariosRepository dadosBancariosRepository;
    private final ColaboradorDocumentosRepository documentosRepository;
    private final ColaboradorDocumentosAnexosRepository documentosAnexosRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final OnboardingValidacoesRepository onboardingValidacoesRepository;

    private final ColaboradorDadosPessoaisMapper dadosMapper;
    private final ColaboradorDependentesMapper dependentesMapper;
    private final ColaboradorEnderecoMapper enderecoMapper;
    private final ColaboradorDadosBancariosMapper dadosBancariosMapper;
    private final ColaboradorDocumentosMapper documentosMapper;
    private final UsuarioMapper usuarioMapper;
    private final ColaboradorMapper colaboradorMapper;

    private final UsuarioAutenticadoService usuarioAutenticadoService;
    private final ColaboradorAutenticadoService colaboradorAutenticadoService;
    private final MinioService minioService;
    private final ColaboradorDependentesRepository dependentesRepository;

    public OnboardingService(
            ColaboradorDadosPessoaisRepository dadosRepository, ColaboradorEnderecoRepository enderecoRepository,
            ColaboradorDadosBancariosRepository dadosBancariosRepository, ColaboradorDocumentosRepository documentosRepository,
            ColaboradorDocumentosAnexosRepository documentosAnexosRepository, UsuarioRepository usuarioRepository,
            ColaboradorRepository colaboradorRepository, OnboardingValidacoesRepository onboardingValidacoesRepository,
            ColaboradorDadosPessoaisMapper dadosMapper, ColaboradorDependentesMapper dependentesMapper,
            ColaboradorEnderecoMapper enderecoMapper,
            ColaboradorDadosBancariosMapper dadosBancariosMapper,
            UsuarioAutenticadoService usuarioAutenticadoService, ColaboradorAutenticadoService colaboradorAutenticadoService,
            PerfilRepository perfilRepository, ColaboradorDocumentosMapper documentosMapper,
            UsuarioMapper usuarioMapper, ColaboradorMapper colaboradorMapper, MinioService minioService, ColaboradorDependentesRepository dependentesRepository
    ) {
        this.dadosRepository = dadosRepository;
        this.enderecoRepository = enderecoRepository;
        this.dadosBancariosRepository = dadosBancariosRepository;
        this.documentosRepository = documentosRepository;
        this.documentosAnexosRepository = documentosAnexosRepository;
        this.usuarioRepository = usuarioRepository;
        this.onboardingValidacoesRepository = onboardingValidacoesRepository;
        this.dependentesMapper = dependentesMapper;
        this.colaboradorMapper = colaboradorMapper;
        this.colaboradorRepository = colaboradorRepository;
        this.dadosMapper = dadosMapper;
        this.enderecoMapper = enderecoMapper;
        this.dadosBancariosMapper = dadosBancariosMapper;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
        this.colaboradorAutenticadoService = colaboradorAutenticadoService;
        this.perfilRepository = perfilRepository;
        this.documentosMapper = documentosMapper;
        this.usuarioMapper = usuarioMapper;
        this.minioService = minioService;
        this.dependentesRepository = dependentesRepository;
    }

    @Transactional(readOnly = true)
    public ColaboradorDadosPessoaisResponse buscarDadosPessoais() {
        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();
        ColaboradorDadosPessoais dadosPessoais = dadosRepository.findByColaborador(colaborador)
                .orElse(null);

        OnboardingValidacoes validacoes = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(colaborador.getId(),
                        OnboardingValidacaoStep.DADOS_PESSOAIS).orElse(null);

        StatusValidacao statusValidacao = validacoes != null ? validacoes.getStatusValidacao() : null;

        String observacao = validacoes != null ? validacoes.getObservacao() : null;

        if (dadosPessoais == null) return null;
        return dadosMapper.toResponse(dadosPessoais, observacao, statusValidacao);

    }

    @Transactional(readOnly = true)
    public ColaboradorEnderecoResponse buscarEndereco() {
        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();
        ColaboradorEndereco endereco = enderecoRepository.findByColaborador(colaborador)
                .orElse(null);

        OnboardingValidacoes validacoes = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(colaborador.getId(),
                        OnboardingValidacaoStep.ENDERECO).orElse(null);

        StatusValidacao statusValidacao = validacoes != null ? validacoes.getStatusValidacao() : null;

        String observacao = validacoes != null ? validacoes.getObservacao() : null;

        if (endereco == null) return null;
        return enderecoMapper.toResponse(endereco, observacao, statusValidacao);
    }


    @Transactional(readOnly = true)
    public DependentesComObservacaoResponseDTO buscarDependentes() {
        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        OnboardingValidacoes validacoes = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(colaborador.getId(),
                        OnboardingValidacaoStep.DEPENDENTES).orElse(null);

        String observacao = validacoes != null ? validacoes.getObservacao() : null;

        StatusValidacao statusValidacao = validacoes != null ? validacoes.getStatusValidacao() : null;

        List<ColaboradorDependentesResponseDTO> dependentes =
                dependentesRepository.findAllByColaborador(colaborador)
                        .stream()
                        .map(dependentesMapper::toResponse)
                        .toList();

        return new DependentesComObservacaoResponseDTO(observacao, statusValidacao, dependentes);

    }

    @Transactional(readOnly = true)
    public ColaboradorDadosBancariosResponseDTO buscarDadosBancarios() {
        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        ColaboradorDadosBancarios dadosBancarios = dadosBancariosRepository.findByColaborador(colaborador)
                .orElse(null);

        OnboardingValidacoes validacoes = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(colaborador.getId(),
                        OnboardingValidacaoStep.DADOS_BANCARIOS).orElse(null);

        StatusValidacao statusValidacao = validacoes != null ? validacoes.getStatusValidacao() : null;

        String observacao = validacoes != null ? validacoes.getObservacao() : null;

        if (dadosBancarios == null) return null;
        return dadosBancariosMapper.toResponse(dadosBancarios, observacao, statusValidacao);

    }

    @Transactional(readOnly = true)
    public ColaboradorDocumentosResponseDTO buscarDocumentos() {
        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        ColaboradorDocumentos documentos = documentosRepository.findByColaborador(colaborador)
                .orElse(null);

        OnboardingValidacoes validacoes = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(colaborador.getId(),
                        OnboardingValidacaoStep.DOCUMENTOS).orElse(null);

        StatusValidacao statusValidacao = validacoes != null ? validacoes.getStatusValidacao() : null;

        String observacao = validacoes != null ? validacoes.getObservacao() : null;

        if (documentos == null) return null;
        return documentosMapper.toResponse(documentos, observacao, statusValidacao);

    }

    @Transactional(readOnly = true)
    public DocumentosAnexosComObservacaoResponseDTO buscarDocumentosAnexos() {
        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        OnboardingValidacoes validacoes = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(colaborador.getId(),
                        OnboardingValidacaoStep.DOCUMENTOS_ANEXOS).orElse(null);

        StatusValidacao statusValidacao = validacoes != null ? validacoes.getStatusValidacao() : null;

        String observacao = validacoes != null ? validacoes.getObservacao() : null;

        List<ColaboradorDocumentosAnexosResponseDTO> documentosAnexos =
                documentosAnexosRepository.findAllByColaborador(colaborador)
                .stream()
                .map(doc -> {

                    String arquivoPath = doc.getArquivoUrl();

                    String arquivoUrl = arquivoPath != null
                            ? minioService.getPresignedUrl(arquivoPath, 60 * 60)
                            : null;

                    return new ColaboradorDocumentosAnexosResponseDTO(
                            doc.getId(),
                            doc.getTipoDocumentoAnexo(),
                            arquivoUrl,
                            doc.getDataUpload(),
                            doc.getDataValidade(),
                            doc.getStatus(),
                            doc.getDataCriacao(),
                            doc.getDataAlteracao()

                    );
                })
                .toList();

        return new DocumentosAnexosComObservacaoResponseDTO(observacao, statusValidacao, documentosAnexos);
    }

    @Transactional(readOnly = true)
    public UsuarioDTOResponse buscarUsuario() {
        Usuario usuario = usuarioAutenticadoService.getUsuario();

        return usuarioRepository.findByLogin(usuario.getLogin())
                .map(usuarioMapper::toDTO)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public ColaboradorDTOResponse buscarColaborador() {
        Usuario usuario = usuarioAutenticadoService.getUsuario();

        return colaboradorRepository.findByUsuarioId(usuario.getId())
                .map(colaboradorMapper::toResponse)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public OnboardingStatusResponse obterStatus() {
        Usuario usuario = usuarioAutenticadoService.getUsuario();

        if (!usuario.isPrimeiroAcesso()) {
            return new OnboardingStatusResponse(false, OnboardingStep.CONCLUIDO);
        }

        try {
            Colaborador colaborador = colaboradorAutenticadoService.getColaborador();
            return new OnboardingStatusResponse(true, colaborador.getOnboardingStep());
        } catch (Exception e) {
            return new OnboardingStatusResponse(true, OnboardingStep.DADOS_PESSOAIS);
        }
    }

    @Transactional
    public void enviarParaAnalise() {
        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();
        colaborador.setOnboardingStep(OnboardingStep.EM_ANALISE);
    }

    @Transactional
    public void concluirOnboarding(Colaborador colaborador) {

        if (colaborador.getOnboardingStep() != OnboardingStep.EM_ANALISE) {
            throw new NegocioException("Onboarding não está em análise");
        }

        colaborador.setOnboardingStep(OnboardingStep.CONCLUIDO);
        colaboradorRepository.save(colaborador);

        Usuario usuarioColaborador = colaborador.getUsuario();

        usuarioColaborador.desmarcarPrimeiroAcesso();

        new HashSet<>(usuarioColaborador.getPerfis())
                .forEach(usuarioColaborador::removerPerfil);

        Perfil perfilColaborador = perfilRepository
                .findByDescricaoAndStatus("COLABORADOR", Status.ATIVO)
                .orElseThrow(() -> new NegocioException("Perfil COLABORADOR não encontrado"));

        usuarioColaborador.adicionarPerfil(perfilColaborador);

        usuarioRepository.save(usuarioColaborador);
    }


}