package com.projeto.erp.modelo;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "colaborador_integracao_legal")
public class ColaboradorIntegracaoLegal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Colaborador colaborador;

    private String idEsocial;
    private String statusEsocial;
    private LocalDate dataEnvioEsocial;
    private boolean retificacaoEsocial;

    private String idRais;
    private String idCaged;
}

