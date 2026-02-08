package com.projeto.erp.dtos;

import java.time.LocalDate;

public record ColaboradorLotacaoCreateDTO(

        Long colaboradorId,

        Long cargoId,
        Long departamentoId,
        Long gestorId,
        Long centroCustoId,
        LocalDate dataInicio

) {}
