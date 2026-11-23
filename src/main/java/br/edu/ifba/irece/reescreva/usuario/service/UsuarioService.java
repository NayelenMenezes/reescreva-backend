package br.edu.ifba.irece.reescreva.usuario.service;

import br.edu.ifba.irece.reescreva.infrastructure.exception.CredenciaisInvalidasException;
import br.edu.ifba.irece.reescreva.infrastructure.exception.RecursoNaoEncontradoException;
import br.edu.ifba.irece.reescreva.infrastructure.exception.RegraDeNegocioException;
import br.edu.ifba.irece.reescreva.usuario.dto.LoginRequestDTO;
import br.edu.ifba.irece.reescreva.usuario.dto.LoginResponseDTO;
import br.edu.ifba.irece.reescreva.usuario.dto.UsuarioRequestDTO;
import br.edu.ifba.irece.reescreva.usuario.dto.UsuarioResponseDTO;
import br.edu.ifba.irece.reescreva.usuario.entity.PerfilUsuario;
import br.edu.ifba.irece.reescreva.usuario.entity.StatusUsuario;
import br.edu.ifba.irece.reescreva.usuario.entity.Usuario;
import br.edu.ifba.irece.reescreva.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementacao das regras de negocio de Usuario.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioService implements UsuarioIService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UsuarioResponseDTO save(UsuarioRequestDTO dto) {
        // Regra 1: Impede cadastro de e-mail duplicado no sistema
        if (usuarioRepository.existsByEmailIgnoreCase(dto.email().trim())) {
            throw new RegraDeNegocioException("Ja existe um usuario cadastrado com este e-mail.");
        }

        Usuario novoUsuario = Usuario.builder()
                .nomeCompleto(dto.nomeCompleto().trim())
                .email(dto.email().trim().toLowerCase())
                .senha(dto.senha())
                .perfil(dto.perfil() != null ? dto.perfil() : PerfilUsuario.COORDENADOR)
                .status(dto.status() != null ? dto.status() : StatusUsuario.ATIVO)
                .build();

        Usuario salvo = usuarioRepository.save(novoUsuario);
        return UsuarioResponseDTO.fromEntity(salvo);
    }

    @Override
    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = findEntityById(id);
        return UsuarioResponseDTO.fromEntity(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = findEntityById(id);
        String emailNormalizado = dto.email().trim().toLowerCase();

        // Regra 1: Se alterou o e-mail, garante que o novo e-mail nao pertence a outro usuario
        if (!usuario.getEmail().equalsIgnoreCase(emailNormalizado)
                && usuarioRepository.existsByEmailIgnoreCase(emailNormalizado)) {
            throw new RegraDeNegocioException("Ja existe outro usuario cadastrado com este e-mail.");
        }

        // Regra 2: Protege o ultimo administrador ativo de perder o perfil ou ser desativado
        boolean deixaraDeSerAdminAtivo = usuario.isAdministrador() && usuario.isAtivo()
                && ((dto.perfil() != null && dto.perfil() != PerfilUsuario.ADMINISTRADOR)
                || (dto.status() != null && dto.status() != StatusUsuario.ATIVO));

        if (deixaraDeSerAdminAtivo) {
            validateActiveAdminExists();
        }

        usuario.setNomeCompleto(dto.nomeCompleto().trim());
        usuario.setEmail(emailNormalizado);
        usuario.setSenha(dto.senha());
        if (dto.perfil() != null) {
            usuario.setPerfil(dto.perfil());
        }
        if (dto.status() != null) {
            usuario.setStatus(dto.status());
        }

        return UsuarioResponseDTO.fromEntity(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioResponseDTO updateStatus(Long id, StatusUsuario newStatus) {
        Usuario usuario = findEntityById(id);

        // Regra 2: Nao permite desativar o unico administrador ativo
        if (usuario.isAdministrador() && usuario.isAtivo() && newStatus != StatusUsuario.ATIVO) {
            validateActiveAdminExists();
        }

        usuario.setStatus(newStatus);
        return UsuarioResponseDTO.fromEntity(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Usuario usuario = findEntityById(id);

        // Regra 2: Nao permite excluir o unico administrador ativo do sistema
        if (usuario.isAdministrador() && usuario.isAtivo()) {
            validateActiveAdminExists();
        }

        usuarioRepository.delete(usuario);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(dto.email().trim())
                .orElseThrow(() -> new CredenciaisInvalidasException("E-mail ou senha invalidos."));

        if (!usuario.getSenha().equals(dto.senha())) {
            throw new CredenciaisInvalidasException("E-mail ou senha invalidos.");
        }

        // Regra 3: Contas inativas ou pendentes nao podem acessar o sistema
        if (!usuario.isAtivo()) {
            throw new CredenciaisInvalidasException(
                    "Esta conta esta com status " + usuario.getStatus() + " e nao pode acessar o sistema."
            );
        }

        return LoginResponseDTO.builder()
                .id(usuario.getId())
                .nomeCompleto(usuario.getNomeCompleto())
                .email(usuario.getEmail())
                .perfil(usuario.getPerfil())
                .mensagem("Login realizado com sucesso.")
                .build();
    }

    private Usuario findEntityById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado com o ID: " + id));
    }

    private void validateActiveAdminExists() {
        long adminsAtivos = usuarioRepository.countByPerfilAndStatus(
                PerfilUsuario.ADMINISTRADOR,
                StatusUsuario.ATIVO
        );
        if (adminsAtivos <= 1) {
            throw new RegraDeNegocioException(
                    "Operacao bloqueada: o sistema precisa manter ao menos um administrador ativo."
            );
        }
    }
}