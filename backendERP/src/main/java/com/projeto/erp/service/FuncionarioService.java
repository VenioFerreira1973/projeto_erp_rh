package com.projeto.erp.service;

import com.projeto.erp.dtos.FuncionarioDTORequest;
import com.projeto.erp.dtos.FuncionarioDTOResponse;
import com.projeto.erp.mapper.FuncionarioMapper;
import com.projeto.erp.modelo.*;
import com.projeto.erp.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final CargoRepository cargoRepository;
    private final DepartamentoRepository departamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final FuncionarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(FuncionarioRepository repository, CargoRepository cargoRepository, DepartamentoRepository departamentoRepository, UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, FuncionarioMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.cargoRepository = cargoRepository;
        this.departamentoRepository = departamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<FuncionarioDTOResponse> listar() {
        List<Funcionario> funcionarios = repository.findAll();

        List<FuncionarioDTOResponse> dtos = new ArrayList<>();
        for (Funcionario funcionario : funcionarios) {
            dtos.add(mapper.toResponse(funcionario));
        }

        return dtos;
    }

    public FuncionarioDTOResponse obter(Long id) {

        Optional<Funcionario> optionalFuncionario = repository.findById(id);

        if (optionalFuncionario.isEmpty()) {
            throw new EntityNotFoundException("Funcionário não encontrado");
        }

        Funcionario funcionario = optionalFuncionario.get();
        return mapper.toResponse(funcionario);
    }

    @Transactional
    public FuncionarioDTOResponse cadastrar(FuncionarioDTORequest dto) {


        Funcionario funcionario = mapper.toEntity(dto);

        Cargo cargo = cargoRepository.findById(dto.cargoId())
                .orElseThrow(() -> new RuntimeException("Cargo não encontrado"));

        Departamento departamento = departamentoRepository.findById(dto.departamentoId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        Usuario usuario = criarUsuarioPadrao(dto.nome());

        funcionario.alterarCargo(cargo);
        funcionario.alterarDepartamento(departamento);

        funcionario.vincularUsuario(usuario);

        repository.save(funcionario);

        return mapper.toResponse(funcionario);
    }

    private Usuario criarUsuarioPadrao(String nomeCompleto) {
        String login = gerarLogin(nomeCompleto);
        String email = gerarEmail(nomeCompleto);

        Perfil perfilFuncionario = perfilRepository.getByDescricao("FUNCIONARIO")
                .orElseThrow(() -> new RuntimeException("Perfil FUNCIONARIO não encontrado"));

        Usuario usuario = new Usuario(login, email, passwordEncoder.encode("1234"));
        usuario.ativar();
        usuario.adicionarPerfil(perfilFuncionario);

        return usuarioRepository.save(usuario);
    }

    private String gerarLogin(String nomeCompleto) {
        String[] partes = nomeCompleto.trim().toLowerCase().split("\\s+");
        return partes[0] + "_" + partes[partes.length - 1];
    }

    private String gerarEmail(String nomeCompleto) {
        return gerarLogin(nomeCompleto) + "@empresa.com";
    }


    public FuncionarioDTOResponse atualizar(Long id, FuncionarioDTORequest dto) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Funcionário não encontrado");
        }

        Funcionario funcionario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado"));
        mapper.updateEntityFromDTO(dto, funcionario);

        return mapper.toResponse(repository.save(funcionario));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Funcionário não encontrado");
        }
        repository.deleteById(id);
    }

    public List<FuncionarioDTOResponse> listarPorDepartamento(Long departamentoId) {
        return repository.findByDepartamentoId(departamentoId)
                .stream().map(mapper::toResponse).toList();
    }

    public List<FuncionarioDTOResponse> listarPorCargo(Long cargoId) {
        return repository.findByCargoId(cargoId)
                .stream().map(mapper::toResponse).toList();
    }

    public List<FuncionarioDTOResponse> listarGerentes() {
        return repository.findByGerenteTrue()
                .stream().map(mapper::toResponse).toList();
    }

}
