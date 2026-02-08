package com.projeto.erp.modelo;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "colaborador_saude_seguranca")
public class ColaboradorSaudeSeguranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Colaborador colaborador;

    private boolean pcd;
    private String tipoDeficiencia;

    private LocalDate exameAdmissionalData;
    private LocalDate examePeriodicoData;
    private LocalDate exameDemissionalData;

    private String asoStatus;
    private String grauRisco;

    private boolean epiObrigatorio;
}
