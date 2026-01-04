package com.projeto.erp.service;

import com.projeto.erp.dtos.PerfilDTO;
import com.projeto.erp.mapper.PerfilMapper;
import com.projeto.erp.modelo.Perfil;
import com.projeto.erp.repository.PerfilRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PerfilService {


    private final PerfilRepository repository;
    private final PerfilMapper mapper;

    public PerfilService(PerfilRepository repository, PerfilMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<PerfilDTO> listar(){
        List<Perfil> perfis = repository.findAll();

        List<PerfilDTO> dtos = new ArrayList<>();
        for (Perfil perfil : perfis) {
            dtos.add(mapper.toDTO(perfil));
        }

        return dtos;

    }

    public PerfilDTO obter(Long id) {

        Optional<Perfil> optionalPerfil = repository.findById(id);

        if (optionalPerfil.isEmpty()) {
            throw new EntityNotFoundException("Perfil não encontrado");
        }

        Perfil perfil = optionalPerfil.get();
        return mapper.toDTO(perfil);
    }

    public PerfilDTO cadastrar(PerfilDTO dto) {
        Perfil perfil = mapper.toEntity(dto);
        Perfil salvo = repository.save(perfil);
        return mapper.toDTO(salvo);
    }

    public PerfilDTO atualizar(Long id, PerfilDTO dto) {

        Perfil perfilExistente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado"));

        mapper.updateEntityFromDTO(dto, perfilExistente);

        Perfil salvo = repository.save(perfilExistente);

        return mapper.toDTO(salvo);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Perfil não encontrado");
        }
        repository.deleteById(id);
    }

}
