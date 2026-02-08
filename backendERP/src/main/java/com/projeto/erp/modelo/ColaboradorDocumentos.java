package com.projeto.erp.modelo;

import com.projeto.erp.dtos.ColaboradorDocumentosRequestDTO;
import com.projeto.erp.enumeracoes.Status;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "colaborador_documentos")
public class ColaboradorDocumentos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;

    @Column(name="cpf", nullable = false, length = 11, unique = true)
    private String cpf;

    @Column(name="pis_pasep", nullable = false)
    private String pisPasep;

    @Column(name="possui_ctps_fisica", nullable = false)
    private boolean possuiCtpsFisica;

    @Column(name="ctps_numero")
    private String ctpsNumero;

    @Column(name="ctps_serie")
    private String ctpsSerie;

    @Column(name="registro_estrangeiro")
    private String registroEstrangeiro;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(name = "data_alteracao", nullable = false)
    private Instant dataAlteracao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private Status status = Status.ATIVO;

    protected ColaboradorDocumentos() {
    }

    private ColaboradorDocumentos(
            Colaborador colaborador,
            String cpf,
            String pisPasep,
            String ctpsNumero,
            String ctpsSerie,
            String registroEstrangeiro
    ) {
        this.colaborador = Objects.requireNonNull(colaborador);
        this.cpf = Objects.requireNonNull(cpf);
        this.pisPasep = pisPasep;
        this.ctpsNumero = ctpsNumero;
        this.ctpsSerie = ctpsSerie;
        this.registroEstrangeiro = registroEstrangeiro;
    }

    public static ColaboradorDocumentos criar(
            Colaborador colaborador,
            ColaboradorDocumentosRequestDTO request
    ) {
        ColaboradorDocumentos entity = new ColaboradorDocumentos(
                colaborador,
                request.cpf(),
                request.pisPasep(),
                request.ctpsNumero(),
                request.ctpsSerie(),
                request.registroEstrangeiro()
        );
        return entity;
    }

    public void atualizar(ColaboradorDocumentosRequestDTO request) {
        this.cpf = request.cpf();
        this.pisPasep = request.pisPasep();
        this.possuiCtpsFisica = request.possuiCtpsFisica();
        this.ctpsNumero = request.ctpsNumero();
        this.ctpsSerie = request.ctpsSerie();
        this.registroEstrangeiro = request.registroEstrangeiro();
    }

    public Long getId() { return id; }
    public Colaborador getColaborador() {
        return colaborador;
    }
    public String getCpf() {return cpf;}
    public String getPisPasep() {return pisPasep;}
    public boolean getPossuiCtpsFisica(){return possuiCtpsFisica;}
    public String getCtpsNumero() {return ctpsNumero;}
    public String getCtpsSerie() {return ctpsSerie;}
    public String getRegistroEstrangeiro() {return registroEstrangeiro;}
    public Status getStatus() {
        return status;
    }
    public Instant getDataCriacao() {
        return dataCriacao;
    }
    public Instant getDataAlteracao() {
        return dataAlteracao;
    }


    public void desativar() {
        this.status = Status.INATIVO;
    }

    public void ativar() {
        this.status = Status.ATIVO;
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
        if (!(o instanceof ColaboradorDocumentos other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
