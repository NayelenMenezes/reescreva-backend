package br.edu.ifba.irece.reescreva.indicacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Dados enviados pelo formulario de indicacao no front-end.
 */
public record IndicacaoRequestDTO(
        @NotBlank(message = "O nome do interessado e obrigatorio.")
        @Size(min = 3, max = 150, message = "O nome deve ter entre 3 e 150 caracteres.")
        String nomeInteressado,

        @NotBlank(message = "O telefone de contato e obrigatorio.")
        @Size(min = 8, max = 20, message = "Informe um telefone valido com DDD.")
        String telefone,

        @NotNull(message = "A idade do interessado e obrigatoria.")
        Integer idade,

        @Size(max = 100, message = "O bairro deve ter no maximo 100 caracteres.")
        String bairroInteressado,

        @Size(max = 150, message = "O nome de quem indicou deve ter no maximo 150 caracteres.")
        String quemIndicou,

        @Size(max = 500, message = "As observacoes devem ter no maximo 500 caracteres.")
        String observacoes,

        Long turmaId
) {
}