package br.edu.ifba.irece.reescreva.usuario.dto;

import br.edu.ifba.irece.reescreva.usuario.entity.PerfilUsuario;
import br.edu.ifba.irece.reescreva.usuario.entity.StatusUsuario;
import br.edu.ifba.irece.reescreva.usuario.entity.Usuario;
import lombok.Builder;

/**
 * Dados seguros retornados pela API (omite a senha).
 */
@Builder
public record UsuarioResponseDTO(
        Long id,
        String nomeCompleto,
        String email,
        PerfilUsuario perfil,
        StatusUsuario status
) {
    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nomeCompleto(usuario.getNomeCompleto())
                .email(usuario.getEmail())
                .perfil(usuario.getPerfil())
                .status(usuario.getStatus())
                .build();
    }
}