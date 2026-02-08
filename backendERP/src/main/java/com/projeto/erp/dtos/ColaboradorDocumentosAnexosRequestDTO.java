package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.TipoDocumentoAnexo;
import java.time.LocalDate;

public record ColaboradorDocumentosAnexosRequestDTO(
        TipoDocumentoAnexo tipoDocumentoAnexo,
        String arquivoUrl,
        LocalDate dataValidade
) {
}

