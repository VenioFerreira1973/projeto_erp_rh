package com.projeto.erp.modelo;

import com.projeto.erp.enumeracoes.MotivoAlteracaoSalario;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "colaborador_salario")
public class ColaboradorSalario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_contrato_id")
    private ColaboradorContrato colaboradorContrato;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_alteracao_salario", nullable = false, length = 30)
    private MotivoAlteracaoSalario motivoAlteracaoSalario;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(name = "data_alteracao", nullable = false)
    private Instant dataAlteracao;

    protected ColaboradorSalario() {}

    public ColaboradorSalario(ColaboradorContrato colaboradorContrato, BigDecimal novoValor, LocalDate dataInicio, MotivoAlteracaoSalario motivo) {
        this.colaboradorContrato = colaboradorContrato;
        this.valor = novoValor;
        this.dataInicio = dataInicio;
        this.motivoAlteracaoSalario = motivo;
        this.dataCriacao = Instant.now();
        this.dataAlteracao = Instant.now();
    }

    public boolean isVigente() {
        return dataFim == null;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getDataInicio() {return dataInicio;}

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
        this.dataAlteracao = Instant.now();
    }
}
