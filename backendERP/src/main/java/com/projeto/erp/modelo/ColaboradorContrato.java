package com.projeto.erp.modelo;

import com.projeto.erp.enumeracoes.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "colaborador_contrato")
public class ColaboradorContrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;


    @ManyToOne(fetch = FetchType.LAZY)
    private Sindicato sindicato;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_jornada")
    private TipoJornada tipoJornada;

    @Column(name = "horas_semanais")
    private Integer horasSemanais;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contrato", nullable = false)
    private TipoContrato tipoContrato;

    @Enumerated(EnumType.STRING)
    @Column(name = "prazo_contrato")
    private PrazoContrato prazoContrato;

    @Column(name = "experiencia_inicio")
    private LocalDate experienciaInicio;

    @Column(name = "experiencia_fim")
    private LocalDate experienciaFim;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vinculo", nullable = false, length = 20)
    private TipoVinculo tipoVinculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "regime_trabalho", nullable = false, length = 20)
    private RegimeTrabalho regimeTrabalho;

    @Enumerated(EnumType.STRING)
    @Column(name = "contrato_status", nullable = false)
    private ContratoStatus contratoStatus;

    @Column(name = "primeiro_emprego")
    private boolean primeiroEmprego;

    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    @Column(name = "data_inicio_vinculo", nullable = false)
    private LocalDate dataInicioVinculo;

    @Column(name = "data_demissao")
    private LocalDate dataDemissao;

    @Column(name = "data_fim_vinculo")
    private LocalDate dataFimVinculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_demissao", length = 20)
    private MotivoDemissao motivoDemissao;

    @OneToMany(
            mappedBy = "colaboradorContrato",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ColaboradorSalario> salarios = new ArrayList<>();

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(name = "data_alteracao", nullable = false)
    private Instant dataAlteracao;

    protected ColaboradorContrato() {}

    public ColaboradorContrato(
            Colaborador colaborador,
            Empresa empresa,
            TipoContrato tipoContrato,
            TipoJornada tipoJornada,
            Integer horasSemanais,
            LocalDate dataAdmissao
    ) {
        this.colaborador = Objects.requireNonNull(colaborador);
        this.empresa = Objects.requireNonNull(empresa);
        this.tipoContrato = Objects.requireNonNull(tipoContrato);
        this.tipoJornada = Objects.requireNonNull(tipoJornada);
        this.horasSemanais = Objects.requireNonNull(horasSemanais);
        this.dataAdmissao = Objects.requireNonNull(dataAdmissao);

        this.contratoStatus = ContratoStatus.ATIVO;
        this.dataInicioVinculo = dataAdmissao;
        this.primeiroEmprego = false;
    }

    public Long getId() {
        return id;
    }

    public Colaborador getcolaborador() {
        return colaborador;
    }

    public Sindicato getSindicato() {
        return sindicato;
    }

    public TipoJornada getTipoJornada() {
        return tipoJornada;
    }

    public Integer getHorasSemanais() {
        return horasSemanais;
    }

    public TipoContrato getTipoContrato() {
        return tipoContrato;
    }

    public PrazoContrato getPrazoContrato() {
        return prazoContrato;
    }

    public LocalDate getExperienciaInicio() {
        return experienciaInicio;
    }

    public LocalDate getExperienciaFim() {
        return experienciaFim;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public TipoVinculo getTipoVinculo() {
        return tipoVinculo;
    }

    public RegimeTrabalho getRegimeTrabalho() {
        return regimeTrabalho;
    }

    public ContratoStatus getContratoStatus() {
        return contratoStatus;
    }

    public boolean isPrimeiroEmprego() {
        return primeiroEmprego;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public LocalDate getDataInicioVinculo() {
        return dataInicioVinculo;
    }

    public LocalDate getDataDemissao() {
        return dataDemissao;
    }

    public LocalDate getDataFimVinculo() {
        return dataFimVinculo;
    }

    public MotivoDemissao getMotivoDemissao() {
        return motivoDemissao;
    }

    public List<ColaboradorSalario> getSalarios() {
        return Collections.unmodifiableList(salarios);
    }

    public Instant getDataCriacao() {
        return dataCriacao;
    }

    public Instant getDataAlteracao() {
        return dataAlteracao;
    }


    @PrePersist
    public void prePersist() {
        this.dataCriacao = Instant.now();
        this.dataAlteracao = this.dataCriacao;
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAlteracao = Instant.now();
    }


    public void adicionarSalario(ColaboradorSalario salario) {
        getSalarioAtual().ifPresent(s -> salario.setDataFim(salario.getDataInicio().minusDays(1)));
        this.salarios.add(salario);
    }

    public Optional<ColaboradorSalario> getSalarioAtual() {
        return salarios.stream()
                .filter(ColaboradorSalario::isVigente)
                .max(Comparator.comparing(ColaboradorSalario::getDataInicio));
    }

    public void alterarSalario(
            BigDecimal novoValor,
            LocalDate dataInicio,
            MotivoAlteracaoSalario motivo
    ) {
        Objects.requireNonNull(novoValor);
        Objects.requireNonNull(dataInicio);
        Objects.requireNonNull(motivo);


        getSalarioAtual().ifPresent(atual ->
                atual.setDataFim(dataInicio.minusDays(1)));

        ColaboradorSalario novo = new ColaboradorSalario(
                this,
                novoValor,
                dataInicio,
                motivo
        );

        salarios.add(novo);
    }

    public void ligar() {
        this.contratoStatus = ContratoStatus.ATIVO;
        this.dataDemissao = null;
        this.dataFimVinculo = null;
    }

    private void validarPeriodo(LocalDate inicio, LocalDate fim) {
        if (fim != null && fim.isBefore(inicio)) {
            throw new IllegalStateException(
                    "Data fim não pode ser anterior à data início"
            );
        }
    }

    public void desligar(LocalDate dataDemissao) {
        if (this.contratoStatus != ContratoStatus.ATIVO) {
            throw new IllegalStateException(
                    "Contrato não está ativo"
            );
        }

        validarPeriodo(this.dataInicioVinculo, dataDemissao);

        this.contratoStatus = ContratoStatus.RESCINDIDO;
        this.dataDemissao = dataDemissao;
        this.dataFimVinculo = dataDemissao;
    }

    public void definirPeriodoExperiencia(
            LocalDate inicio,
            LocalDate fim
    ) {
        if (inicio == null && fim != null) {
            throw new IllegalStateException(
                    "Data fim de experiência exige data início"
            );
        }

        if (inicio != null && fim != null && fim.isBefore(inicio)) {
            throw new IllegalStateException(
                    "Experiência fim não pode ser antes do início"
            );
        }

        this.experienciaInicio = inicio;
        this.experienciaFim = fim;
    }

    public void alterarEmpresa(Empresa empresa) {
        this.empresa = empresa;
        this.dataAlteracao = Instant.now();
    }

    public void alterarColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
        this.dataAlteracao = Instant.now();
    }

}
