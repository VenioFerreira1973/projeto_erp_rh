package com.projeto.erp.service;

import com.projeto.erp.dtos.ColaboradorDocumentosAnexosRequestDTO;
import com.projeto.erp.dtos.ColaboradorDocumentosAnexosResponseDTO;
import com.projeto.erp.dtos.DocumentosAnexosComObservacaoResponseDTO;
import com.projeto.erp.enumeracoes.OnboardingValidacaoStep;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.enumeracoes.TipoDocumentoAnexo;
import com.projeto.erp.exception.NegocioException;
import com.projeto.erp.mapper.ColaboradorDocumentosAnexosMapper;
import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorDocumentosAnexos;
import com.projeto.erp.modelo.OnboardingValidacoes;
import com.projeto.erp.repository.ColaboradorDocumentosAnexosRepository;
import com.projeto.erp.repository.ColaboradorRepository;
import com.projeto.erp.repository.OnboardingValidacoesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ColaboradorDocumentosAnexosService {

    private final ColaboradorDocumentosAnexosRepository repository;
    private final ColaboradorRepository colaboradorRepository;
    private final ColaboradorDocumentosAnexosMapper mapper;
    private final ColaboradorAutenticadoService colaboradorAutenticadoService;
    private final MinioService minioService;
    private final OnboardingValidacoesRepository onboardingValidacoesRepository;

    public ColaboradorDocumentosAnexosService(
            ColaboradorDocumentosAnexosRepository repository,
            ColaboradorRepository colaboradorRepository,
            ColaboradorDocumentosAnexosMapper mapper, ColaboradorAutenticadoService colaboradorAutenticadoService, MinioService minioService, OnboardingValidacoesRepository onboardingValidacoesRepository, DocumentosAnexosService documentosAnexosService
    ) {
        this.repository = repository;
        this.colaboradorRepository = colaboradorRepository;
        this.mapper = mapper;
        this.colaboradorAutenticadoService = colaboradorAutenticadoService;
        this.minioService = minioService;
        this.onboardingValidacoesRepository = onboardingValidacoesRepository;
    }


    @Transactional(readOnly = true)
    public DocumentosAnexosComObservacaoResponseDTO obterPorColaborador(Long colaboradorId) {

        Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Colaborador não encontrado com id: " + colaboradorId
                ));

        OnboardingValidacoes validacoes = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(colaborador.getId(),
                        OnboardingValidacaoStep.DOCUMENTOS_ANEXOS).orElse(null);

        StatusValidacao statusValidacao = validacoes != null ? validacoes.getStatusValidacao() : null;

        String observacao = validacoes != null ? validacoes.getObservacao() : null;

        List<ColaboradorDocumentosAnexosResponseDTO> documentosAnexos =
                repository.findAllByColaborador(colaborador)
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

    @Transactional
    public ColaboradorDocumentosAnexosResponseDTO cadastrar(
            Long colaboradorId,
            ColaboradorDocumentosAnexosRequestDTO request
    ) {
        Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Colaborador não encontrado com id: " + colaboradorId
                ));

        ColaboradorDocumentosAnexos documento = repository
                .findByColaboradorAndArquivoUrl(colaborador, request.arquivoUrl())
                .map(existing -> {
                    mapper.updateEntity(request, existing);
                    return existing;
                })
                .orElseGet(() -> mapper.toEntity(request, colaborador));

        return mapper.toResponse(repository.save(documento));
    }


    @Transactional
    public ColaboradorDocumentosAnexosResponseDTO atualizar(
            Long id,
            ColaboradorDocumentosAnexosRequestDTO request
    ) {
        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        ColaboradorDocumentosAnexos documentosAnexos = repository
                .findByIdAndColaborador(id, colaborador)
                .orElseThrow(() -> new NegocioException("Dependente não encontrado"));

        mapper.updateEntity(request, documentosAnexos);
        return mapper.toResponse(documentosAnexos);
    }

    @Transactional
    public ColaboradorDocumentosAnexosResponseDTO corrigir(
            String tipoDocumentoAnexo,
            MultipartFile arquivo
    ) {

        Colaborador colaborador = colaboradorAutenticadoService.getColaborador();

        OnboardingValidacoes validacao = onboardingValidacoesRepository
                .findByColaboradorIdAndOnboardingValidacaoStep(
                        colaborador.getId(),
                        OnboardingValidacaoStep.DOCUMENTOS_ANEXOS
                )
                .orElseThrow(() -> new IllegalStateException(
                        "Validação de documentos anexos não encontrada"
                ));

        if (validacao.getStatusValidacao() != StatusValidacao.REPROVADO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Correção permitida apenas quando status = REPROVADO"
            );
        }

        TipoDocumentoAnexo tipoDocumento;
        try {
            tipoDocumento = TipoDocumentoAnexo.valueOf(tipoDocumentoAnexo);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo de documento inválido"
            );
        }

        ColaboradorDocumentosAnexos documento = repository
                .findByColaboradorAndTipoDocumentoAnexo(
                        colaborador,
                        tipoDocumento
                ).orElseThrow(() -> new EntityNotFoundException(
                        "Documento anexo não encontrado para o tipo informado"
                ));

        String objectName = gerarObjectName(arquivo);
        minioService.uploadFile(arquivo, objectName);
        documento.atualizarArquivoUrl(objectName);

        repository.save(documento);
        validacao.alterarStatus(StatusValidacao.PENDENTE);
        validacao.atualizarObservacao(null);
        onboardingValidacoesRepository.save(validacao);

        return mapper.toResponse(documento);
    }

    private String gerarObjectName(MultipartFile file) {
        String original = file.getOriginalFilename();
        String extensao = original != null && original.contains(".")
                ? original.substring(original.lastIndexOf("."))
                : "";

        return "onboarding/"
                + UUID.randomUUID()
                + extensao;
    }

}
