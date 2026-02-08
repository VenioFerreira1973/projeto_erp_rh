package com.projeto.erp.modelo;

import com.projeto.erp.enumeracoes.UsuarioStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;
import java.time.Instant;
import java.util.Objects;

@Setter
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "email_pessoal", unique = true, nullable = false)
    private String emailPessoal;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "primeiro_acesso", nullable = false)
    private boolean primeiroAcesso;

    @Enumerated(EnumType.STRING)
    @Column(name = "usuario_status", nullable = false, length = 20)
    private UsuarioStatus status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(name = "data_alteracao", nullable = false)
    private Instant dataAlteracao;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_perfil",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id")
    )
    private Set<Perfil> perfis = new HashSet<>();

    public Usuario(){}

    public Usuario(String login, String emailPessoal, String senha, UsuarioStatus status) {
        this.login = login;
        this.emailPessoal = emailPessoal;
        this.senha = senha;
        this.status = UsuarioStatus.ATIVO;
    }

    public void marcarPrimeiroAcesso(){this.primeiroAcesso = true;}

    public void desmarcarPrimeiroAcesso(){this.primeiroAcesso = false;}

    public boolean isPrimeiroAcesso() {
        return primeiroAcesso;
    }

    public void alterarEmailPessoal(String emailPessoal) {
        this.emailPessoal = emailPessoal;
    }

    public void alterarSenha(String senhaCriptografada) {
        this.senha = senhaCriptografada;
    }

    public void ativar() {
        this.status = UsuarioStatus.ATIVO;
    }

    public void desativar() {
        this.status = UsuarioStatus.INATIVO;
    }

    public void adicionarPerfil(Perfil perfil) {
        this.perfis.add(perfil);
    }

    public void removerPerfil(Perfil perfil) {
        this.perfis.remove(perfil);
    }

    public void definirPerfis(Set<Perfil> perfis) {
        this.perfis.clear();
        if (perfis != null) {
            this.perfis.addAll(perfis);
        }
    }

    public boolean isAtivo() {
        return this.status == UsuarioStatus.ATIVO;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getEmailPessoal() {
        return emailPessoal;
    }

    public String getSenha() {
        return senha;
    }

    public UsuarioStatus getStatus() { return status; }

    public Instant getDataCriacao() {
        return dataCriacao;
    }

    public Instant getDataAlteracao() {
        return dataAlteracao;
    }

    public Set<Perfil> getPerfis() {
        return perfis;
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
        if (!(o instanceof Usuario other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
