package com.projeto.erp.modelo;

import com.projeto.erp.dtos.ColaboradorEnderecoRequest;
import com.projeto.erp.enumeracoes.Status;
import com.projeto.erp.enumeracoes.TipoEndereco;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "colaborador_endereco")
public class ColaboradorEndereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_endereco", length = 20, nullable = false)
    private TipoEndereco tipoEndereco;

    @Column(name = "cep", length = 8, nullable = false)
    private String cep;

    @Column(name = "logradouro", length = 150, nullable = false)
    private String logradouro;

    @Column(name = "numero", length = 10, nullable = false)
    private String numero;

    @Column(name = "complemento", length = 100)
    private String complemento;

    @Column(name = "bairro", length = 100, nullable = false)
    private String bairro;

    @Column(name = "municipio", length = 100, nullable = false)
    private String municipio;

    @Column(name = "uf", length = 2, nullable = false)
    private String uf;

    @Column(name = "pais", length = 60, nullable = false)
    private String pais;


    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(name = "data_alteracao", nullable = false)
    private Instant dataAlteracao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private Status status = Status.ATIVO;

    public ColaboradorEndereco() {
    }

    public ColaboradorEndereco(
            String bairro,
            String cep,
            String complemento,
            TipoEndereco tipoEndereco,
            String logradouro,
            String municipio,
            String numero,
            String pais,
            String uf) {
        this.bairro = bairro;
        this.cep =  cep;
        this.complemento = complemento;
        this.tipoEndereco = tipoEndereco;
        this.logradouro = logradouro;
        this.municipio = municipio;
        this.numero = numero;
        this.pais = pais;
        this.uf = uf;


    }

    public static ColaboradorEndereco criar(
            Colaborador colaborador,
            ColaboradorEnderecoRequest request
    ) {
        ColaboradorEndereco entity = new ColaboradorEndereco(
                request.bairro(),
                request.cep(),
                request.complemento(),
                request.tipoEndereco(),
                request.logradouro(),
                request.municipio(),
                request.numero(),
                request.pais(),
                request.uf()

        );
        entity.colaborador = Objects.requireNonNull(colaborador);
        return entity;
    }

    public void atualizar(ColaboradorEnderecoRequest request) {
        this.bairro = request.bairro();
        this.cep = request.cep();
        this.logradouro = request.logradouro();
        this.numero = request.numero();
        this.complemento =  request.complemento();
        this.tipoEndereco = request.tipoEndereco();
        this.municipio = request.municipio();
        this.uf = request.uf();
        this.pais = request.pais();
    }

    public String getBairro() {return bairro;}
    public String getMunicipio() {return municipio;}
    public String getLogradouro() {return logradouro;}
    public String getNumero() {return numero;}
    public String getComplemento() {return complemento;}
    public String getUf() {return uf;}
    public TipoEndereco getTipoEndereco() {return tipoEndereco;}
    public Colaborador getColaborador() {return colaborador;}
    public String getCep() {return cep;}
    public Long getId() {return id;}
    public Status getStatus() {return status;}
    public Instant getDataAlteracao() {return dataAlteracao;}
    public Instant getDataCriacao(){return dataCriacao; }
    public String getPais() {return pais;}

    public void ativar() {
        this.status = Status.ATIVO;
    }

    public void desativar() {
        this.status = Status.INATIVO;
    }

    public boolean isExterior() {
        return !"Brasil".equalsIgnoreCase(pais);
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
        if (!(o instanceof ColaboradorEndereco other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
