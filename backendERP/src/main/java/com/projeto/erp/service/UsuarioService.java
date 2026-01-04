package com.projeto.erp.service;

import com.projeto.erp.dtos.UsuarioDTORequest;
import com.projeto.erp.dtos.UsuarioDTOResponse;
import com.projeto.erp.mapper.UsuarioMapper;
import com.projeto.erp.modelo.Perfil;
import com.projeto.erp.modelo.Usuario;
import com.projeto.erp.repository.PerfilRepository;
import com.projeto.erp.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PerfilRepository perfilRepository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PerfilRepository perfilRepository, UsuarioMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.perfilRepository = perfilRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioDTOResponse> listar(){
        List<Usuario> usuarios = repository.findAll();

        List<UsuarioDTOResponse> dtos = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            dtos.add(mapper.toDTO(usuario));
        }

        return dtos;
    }

    public UsuarioDTOResponse obter(Long id) {

        Optional<Usuario> optionalUsuario = repository.findById(id);

        if (optionalUsuario.isEmpty()) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }

        Usuario usuario = optionalUsuario.get();
        return mapper.toDTO(usuario);
    }

    public UsuarioDTOResponse cadastrar(UsuarioDTORequest dto) {
        Usuario usuario = mapper.toEntity(dto);
        Usuario salvo = repository.save(usuario);
        return mapper.toDTO(salvo);
    }

    public UsuarioDTOResponse atualizar(Long id, UsuarioDTORequest dto) {

        Usuario usuarioExistente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        mapper.updateEntityFromDTO(dto, usuarioExistente);

        if (dto.senha() != null) {
            usuarioExistente.alterarSenha(passwordEncoder.encode(dto.senha()));
        }

        Usuario salvo = repository.save(usuarioExistente);

        return mapper.toDTO(salvo);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        repository.deleteById(id);
    }

    private Usuario criarUsuarioPadrao(String nomeCompleto) {

        String login = gerarLogin(nomeCompleto);
        String email = gerarEmail(nomeCompleto);

        Perfil perfilFuncionario = perfilRepository.getByDescricao("FUNCIONARIO")
                .orElseThrow(() -> new RuntimeException("Perfil FUNCIONARIO não encontrado"));

        Usuario usuario = new Usuario(login, email, passwordEncoder.encode("1234"));
        usuario.ativar();
        usuario.adicionarPerfil(perfilFuncionario);

        return repository.save(usuario);
    }

    private String gerarLogin(String nomeCompleto) {
        String[] partes = nomeCompleto.trim().toLowerCase().split("\\s+");
        return partes[0] + "_" + partes[partes.length - 1];
    }

    private String gerarEmail(String nomeCompleto) {
        return gerarLogin(nomeCompleto) + "@empresa.com";
    }



}
