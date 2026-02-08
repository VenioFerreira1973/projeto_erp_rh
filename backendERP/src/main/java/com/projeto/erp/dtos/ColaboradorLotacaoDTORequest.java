package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.*;

public record ColaboradorLotacaoDTORequest(
        Long colaboradorId,
        Long cargoId,
        Long departamentoId,
        Long gestorId,
        Long centroCustoId,

        String cargoCbo,
        NivelCargo nivelCargo
) {
}

