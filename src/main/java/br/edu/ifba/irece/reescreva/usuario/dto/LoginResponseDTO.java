package br.edu.ifba.irece.reescreva.usuario.dto;

import br.edu.ifba.irece.reescreva.usuario.entity.PerfilUsuario;
import lombok.Builder;

@Builder
public record LoginResponseDTO(
        Long id,
        String nomeCompleto,
        String email,
        PerfilUsuario perfil,
        String mensagem
) {
}