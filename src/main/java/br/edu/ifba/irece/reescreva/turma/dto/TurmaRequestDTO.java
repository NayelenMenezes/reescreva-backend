package br.edu.ifba.irece.reescreva.turma.dto;

import br.edu.ifba.irece.reescreva.turma.entity.Turno;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Dados recebidos para cadastro ou atualizacao de uma turma.
 */
public record TurmaRequestDTO(
        @NotBlank(message = "O nome do local de ensino e obrigatorio.")
        @Size(min = 3, max = 150, message = "O nome do local deve ter entre 3 e 150 caracteres.")
        String nomeLocal,

        @NotBlank(message = "O bairro e obrigatorio.")
        @Size(max = 100, message = "O bairro deve ter no maximo 100 caracteres.")
        String bairro,

        @NotBlank(message = "O endereco e obrigatorio.")
        @Size(max = 255, message = "O endereco deve ter no maximo 255 caracteres.")
        String endereco,

        @NotNull(message = "O turno e obrigatorio.")
        Turno turno,

        @NotNull(message = "A quantidade total de vagas e obrigatoria.")
        @Min(value = 1, message = "A turma deve oferecer ao menos 1 vaga.")
        Integer vagasTotais,

        Double latitude,
        Double longitude,
        Boolean ativa
) {
}