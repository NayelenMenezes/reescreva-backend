package br.edu.ifba.irece.reescreva.usuario.dto;

import br.edu.ifba.irece.reescreva.usuario.entity.PerfilUsuario;
import br.edu.ifba.irece.reescreva.usuario.entity.StatusUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Dados recebidos para cadastro ou atualizacao de usuario.
 */
public record UsuarioRequestDTO(
        @NotBlank(message = "O nome completo e obrigatorio.")
        @Size(min = 3, max = 150, message = "O nome deve ter entre 3 e 150 caracteres.")
        String nomeCompleto,

        @NotBlank(message = "O e-mail e obrigatorio.")
        @Email(message = "Formato de e-mail invalido.")
        String email,

        @NotBlank(message = "A senha e obrigatoria.")
        @Size(min = 6, message = "A senha deve ter no minimo 6 caracteres.")
        String senha,

        PerfilUsuario perfil,
        StatusUsuario status
) {
}