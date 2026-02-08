package com.projeto.erp.repository;

import com.projeto.erp.enumeracoes.TipoDocumentoAnexo;
import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.modelo.ColaboradorDependentes;
import com.projeto.erp.modelo.ColaboradorDocumentosAnexos;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ColaboradorDocumentosAnexosRepository
        extends JpaRepository<ColaboradorDocumentosAnexos, Long> {

    List<ColaboradorDocumentosAnexos> findAllByColaborador(Colaborador colaborador);

    Optional<ColaboradorDocumentosAnexos> findByIdAndColaborador(
            Long id,
            Colaborador colaborador
    );

    Optional<ColaboradorDocumentosAnexos> findByColaboradorAndArquivoUrl(Colaborador colaborador, String s);

    Optional<ColaboradorDocumentosAnexos> findByColaboradorAndTipoDocumentoAnexo(Colaborador colaborador, TipoDocumentoAnexo tipoDocumentoAnexo);
}
