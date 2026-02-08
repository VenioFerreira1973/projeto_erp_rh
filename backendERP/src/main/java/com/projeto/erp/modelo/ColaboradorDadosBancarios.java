package com.projeto.erp.modelo;

import com.projeto.erp.dtos.ColaboradorDadosBancariosRequestDTO;
import com.projeto.erp.enumeracoes.FormaPagamento;
import com.projeto.erp.enumeracoes.Status;
import com.projeto.erp.enumeracoes.TipoConta;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "colaborador_dados_bancarios")
public class ColaboradorDadosBancarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;

    @Column(name="banco_codigo", nullable = false)
    private String bancoCodigo;

    @Column(name="banco_nome", nullable = false)
    private String bancoNome;

    @Column(nullable = false)
    private String agencia;

    @Column(nullable = false)
    private String conta;

    @Column(nullable = false)
    private String digitoConta;

    @Enumerated(EnumType.STRING)
    @Column(name="tipo_conta", nullable = false)
    private TipoConta tipoConta;

    @Column(name="chave_pix")
    private String chavePix;


    @Enumerated(EnumType.STRING)
    @Column(name="forma_pagamento", nullable = false)
    private FormaPagamento formaPagamento;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(name = "data_alteracao", nullable = false)
    private Instant dataAlteracao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private Status status = Status.ATIVO;

    protected ColaboradorDadosBancarios(){

    }

    private ColaboradorDadosBancarios(
            String bancoCodigo,
            String bancoNome,
            String agencia,
            String conta,
            String digitoConta,
            TipoConta tipoConta,
            String chavePix,
            FormaPagamento formaPagamento
    ) {
        this.bancoCodigo = Objects.requireNonNull(bancoCodigo);
        this.bancoNome = Objects.requireNonNull(bancoNome);
        this.agencia = Objects.requireNonNull(agencia);
        this.conta = Objects.requireNonNull(conta);
        this.digitoConta = Objects.requireNonNull(digitoConta);
        this.tipoConta = Objects.requireNonNull(tipoConta);
        this.formaPagamento = Objects.requireNonNull(formaPagamento);
        this.chavePix = chavePix;
    }

    public static ColaboradorDadosBancarios criar(
            Colaborador colaborador,
            ColaboradorDadosBancariosRequestDTO request
    ) {
        ColaboradorDadosBancarios entity = new ColaboradorDadosBancarios(
                request.bancoCodigo(),
                request.bancoNome(),
                request.agencia(),
                request.conta(),
                request.digitoConta(),
                request.tipoConta(),
                request.chavePix(),
                request.formaPagamento()
        );
        entity.colaborador = Objects.requireNonNull(colaborador);
        return entity;
    }

    public void atualizar(ColaboradorDadosBancariosRequestDTO request) {
        this.bancoCodigo = request.bancoCodigo();
        this.bancoNome = request.bancoNome();
        this.agencia = request.agencia();
        this.conta = request.conta();
        this.digitoConta = request.digitoConta();
        this.tipoConta = request.tipoConta();
        this.chavePix = request.chavePix();
        this.formaPagamento = request.formaPagamento();
    }

    public Long getId() { return id; }
    public Colaborador getColaborador() {
        return colaborador;
    }
    public TipoConta getTipoConta() { return tipoConta; }
    public String getBancoCodigo() { return bancoCodigo; }
    public String getBancoNome() { return bancoNome; }
    public String getAgencia() { return agencia; }
    public String getConta() { return conta; }
    public String getDigitoConta() { return digitoConta; }
    public String getChavePix() { return chavePix; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public Status getStatus() { return status; }
    public Instant getDataCriacao() { return dataCriacao; }
    public Instant getDataAlteracao() { return dataAlteracao; }


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
        if (!(o instanceof ColaboradorDadosBancarios other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


