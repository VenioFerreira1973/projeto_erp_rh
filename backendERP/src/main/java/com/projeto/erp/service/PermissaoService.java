package com.projeto.erp.service;

import com.projeto.erp.dtos.PermissaoDTO;
import com.projeto.erp.mapper.PermissaoMapper;
import com.projeto.erp.modelo.Permissao;
import com.projeto.erp.repository.PermissaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissaoService {

    private final PermissaoRepository repository;
    private final PermissaoMapper mapper;

    public PermissaoService(PermissaoRepository repository, PermissaoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<PermissaoDTO> listar(){
        List<Permissao> permissoes = repository.findAll();

        List<PermissaoDTO> dtos = new ArrayList<>();
        for (Permissao permissao : permissoes) {
            dtos.add(mapper.toDTO(permissao));
        }

        return dtos;

    }

    public PermissaoDTO obter(Long id) {

        Optional<Permissao> optionalPermissao = repository.findById(id);

        if (optionalPermissao.isEmpty()) {
            throw new EntityNotFoundException("Permissão não encontrado");
        }

        Permissao permissao = optionalPermissao.get();
        return mapper.toDTO(permissao);
    }

    public PermissaoDTO cadastrar(PermissaoDTO dto) {
        Permissao permissao = mapper.toEntity(dto);
        Permissao salvo = repository.save(permissao);
        return mapper.toDTO(salvo);
    }

    public PermissaoDTO atualizar(Long id, PermissaoDTO dto) {

        Permissao permissaoExistente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrado"));

        mapper.updateEntityFromDTO(dto, permissaoExistente);

        Permissao salvo = repository.save(permissaoExistente);

        return mapper.toDTO(salvo);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Permissão não encontrado");
        }
        repository.deleteById(id);
    }

}
