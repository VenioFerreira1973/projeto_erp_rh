package com.projeto.erp.modelo;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "colaborador_auditoria")
public class ColaboradorAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Colaborador colaborador;

    private UUID usuarioSistemaId;

    private Instant dataCriacao;
    private Instant dataAtualizacao;

    private String criadoPor;
    private String atualizadoPor;

    private String origemIntegracao;
    private String hashIntegridade;
}

