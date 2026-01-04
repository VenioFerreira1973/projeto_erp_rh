package com.projeto.erp.modelo;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

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

    public Usuario(String login, String email, String senha) {
        this.login = login;
        this.email = email;
        this.senha = senha;
        this.ativo = true;
    }

    public void alterarLogin(String login) {
        this.login = login;
    }

    public void alterarEmail(String email) {
        this.email = email;
    }

    public void alterarSenha(String senhaCriptografada) {
        this.senha = senhaCriptografada;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
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

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

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
