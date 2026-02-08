package com.projeto.erp.modelo;

import com.projeto.erp.dtos.ColaboradorDocumentosAnexosRequestDTO;
import com.projeto.erp.enumeracoes.Status;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.enumeracoes.TipoDocumentoAnexo;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "colaborador_documento_anexo")
public class ColaboradorDocumentosAnexos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;

    @Enumerated(EnumType.STRING)
    @Column(name="tipo_documento_anexo", nullable = false)
    private TipoDocumentoAnexo tipoDocumentoAnexo;

    @Column(name = "arquivo_url", nullable = false)
    private String arquivoUrl;

    @Column(name="data_upload")
    private Instant dataUpload;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private Status status = Status.ATIVO;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(name = "data_alteracao", nullable = false)
    private Instant dataAlteracao;

    protected ColaboradorDocumentosAnexos() {
    }

    private ColaboradorDocumentosAnexos(
            TipoDocumentoAnexo tipoDocumentoAnexo,
            String arquivoUrl,
            LocalDate dataValidade
    ) {
        this.tipoDocumentoAnexo = Objects.requireNonNull(tipoDocumentoAnexo);
        this.arquivoUrl = Objects.requireNonNull(arquivoUrl);
        this.dataValidade = dataValidade;
    }

    public static ColaboradorDocumentosAnexos criar(
            Colaborador colaborador,
            ColaboradorDocumentosAnexosRequestDTO request
    ) {
        ColaboradorDocumentosAnexos entity = new ColaboradorDocumentosAnexos(
                request.tipoDocumentoAnexo(),
                request.arquivoUrl(),
                request.dataValidade()
        );

        entity.colaborador = Objects.requireNonNull(colaborador);
        entity.dataUpload = Instant.now();

        return entity;
    }

    public void atualizar(ColaboradorDocumentosAnexosRequestDTO request) {
        this.tipoDocumentoAnexo = request.tipoDocumentoAnexo();
        this.dataValidade = request.dataValidade();

        if (request.arquivoUrl() != null && !request.arquivoUrl().isBlank()) {
            this.arquivoUrl = request.arquivoUrl();
        }
    }

    public void desativar() {
        this.status = Status.INATIVO;
    }

    public void ativar() {
        this.status = Status.ATIVO;
    }

    public Long getId() {
        return id;
    }
    public Colaborador getColaborador() {
        return colaborador;
    }
    public TipoDocumentoAnexo getTipoDocumentoAnexo() {
        return tipoDocumentoAnexo;
    }
    public String getArquivoUrl() {
        return arquivoUrl;
    }
    public Instant getDataUpload() {
        return dataUpload;
    }
    public LocalDate getDataValidade() {
        return dataValidade;
    }
    public Status getStatus() {
        return status;
    }
    public Instant getDataCriacao() {
        return dataCriacao;
    }
    public Instant getDataAlteracao() {
        return dataAlteracao;
    }

    public void atualizarDataUpload(Instant dataUpload)
    {
        this.dataUpload = dataUpload;
    }

    public void atualizarArquivoUrl(String arquivoUrl)
    {
        this.arquivoUrl = arquivoUrl;
        this.dataAlteracao = Instant.now();
    }


    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.dataCriacao = now;
        this.dataAlteracao = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataAlteracao = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ColaboradorDocumentosAnexos other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}