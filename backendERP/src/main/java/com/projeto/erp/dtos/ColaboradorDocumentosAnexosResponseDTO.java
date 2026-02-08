package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.Status;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.enumeracoes.TipoDocumentoAnexo;

import java.time.Instant;
import java.time.LocalDate;

public record ColaboradorDocumentosAnexosResponseDTO(
        Long id,
        TipoDocumentoAnexo tipoDocumentoAnexo,
        String arquivoUrl,
        Instant dataUpload,
        LocalDate dataValidade,
        Status status,
        Instant dataCriacao,
        Instant dataAlteracao
) {
}
