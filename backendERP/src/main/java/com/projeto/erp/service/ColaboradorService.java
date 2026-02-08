package com.projeto.erp.service;

import com.projeto.erp.dtos.ColaboradorDTOResponse;
import com.projeto.erp.dtos.UsuarioCreateDTORequest;
import com.projeto.erp.dtos.ColaboradorCreateDTOResponse;
import com.projeto.erp.dtos.UsuarioUpdateDTO;
import com.projeto.erp.enumeracoes.OnboardingStep;
import com.projeto.erp.enumeracoes.Status;
import com.projeto.erp.enumeracoes.UsuarioStatus;
import com.projeto.erp.mapper.ColaboradorMapper;
import com.projeto.erp.modelo.*;
import com.projeto.erp.repository.*;
import com.projeto.erp.security.PasswordGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ColaboradorService {

    private final ColaboradorRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final ColaboradorMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public ColaboradorService(
            ColaboradorRepository repository,
            UsuarioRepository usuarioRepository,
            PerfilRepository perfilRepository,
            ColaboradorMapper mapper,
            PasswordEncoder passwordEncoder, EmailService emailService
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional(readOnly = true)
    public List<ColaboradorDTOResponse> listarCandidatos() {
        return repository.findByOnboardingStepNot(OnboardingStep.CONCLUIDO)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ColaboradorDTOResponse> listarColaboradores() {
        return repository.findByOnboardingStep(OnboardingStep.CONCLUIDO)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ColaboradorDTOResponse> listarPorOnboardingStep(OnboardingStep step) {
        return repository.findByOnboardingStep(step)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ColaboradorDTOResponse obter(Long id) {
        Colaborador colaborador = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado"));

        return mapper.toResponse(colaborador);
    }

    @Transactional
    public ColaboradorCreateDTOResponse cadastrar(UsuarioCreateDTORequest dto) {
        String ultima = repository.findUltimaMatricula();
        String matricula = gerarMatricula(ultima);

        Colaborador colaborador = mapper.fromCreateDTO(dto);
        String emailCorporativo = gerarEmailCorporativo(dto.nome());

        colaborador.setEmailCorporativo(emailCorporativo);
        colaborador.setMatricula(matricula);

        String senhaTemporaria = PasswordGenerator.gerarSenhaTemporaria(8);

        String login = gerarLogin(dto.nome());

        Usuario usuario = criarUsuarioInicial(login, dto.emailPessoal(), senhaTemporaria);

        colaborador.vincularUsuario(usuario);

        Colaborador salvo = repository.save(colaborador);

        emailService.enviarSenhaNova(dto.emailPessoal(), login, senhaTemporaria);

        return mapper.toCreateResponse(salvo, senhaTemporaria, emailCorporativo);
    }

    private Usuario criarUsuarioInicial(String login, String emailPessoal, String senhaTemporaria) {

        Perfil perfilCandidato = perfilRepository.findByDescricaoAndStatus("CANDIDATO", Status.ATIVO)
                .orElseThrow(() -> new RuntimeException("Perfil CANDIDATO não encontrado"));


        Usuario usuario = new Usuario(
                login,
                emailPessoal,
                passwordEncoder.encode(senhaTemporaria),
                UsuarioStatus.ATIVO
        );

        usuario.ativar();
        usuario.marcarPrimeiroAcesso();
        usuario.adicionarPerfil(perfilCandidato);

        return usuarioRepository.save(usuario);
    }

    private String gerarLogin(String nomeCompleto) {
        String[] partes = nomeCompleto.trim().toLowerCase().split("\\s+");
        return partes[0] + "_" + partes[partes.length - 1];
    }

    private String gerarEmailCorporativo(String nomeCompleto) {
        return gerarLogin(nomeCompleto) + "@empresa.com";
    }

    private String gerarMatricula(String matriculaFinal) {

        if (matriculaFinal == null) {
            return "MAT000001";
        }

        String prefixo = matriculaFinal.substring(0, 3);
        String numeroStr = matriculaFinal.substring(3);

        int numero = Integer.parseInt(numeroStr);
        numero++;

        int tamanhoOriginal = numeroStr.length();

        return String.format("%s%0" + tamanhoOriginal + "d", prefixo, numero);
    }

    @Transactional
    public ColaboradorDTOResponse atualizarEmail(Long id, UsuarioUpdateDTO dto) {
        Colaborador f = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado com id: " + id));

        Usuario usuario = f.getUsuario();
        if (usuario == null) {
            throw new IllegalStateException("Funcionário não possui usuário associado.");
        }

        usuario.alterarEmailPessoal(dto.emailPessoal());
        usuarioRepository.save(usuario);

        return mapper.toResponse(f);
    }
}
