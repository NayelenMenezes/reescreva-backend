package br.edu.ifba.irece.reescreva.indicacao.dto;

import br.edu.ifba.irece.reescreva.indicacao.entity.Indicacao;
import br.edu.ifba.irece.reescreva.indicacao.entity.StatusIndicacao;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Dados retornados pela API com resumo da turma vinculada (quando houver).
 */
@Builder
public record IndicacaoResponseDTO(
        Long id,
        String nomeInteressado,
        String telefone,
        Integer idade,
        String bairroInteressado,
        String quemIndicou,
        String observacoes,
        StatusIndicacao status,
        LocalDateTime dataIndicacao,
        Long turmaId,
        String turmaNomeLocal,
        String turmaTurno
) {
    public static IndicacaoResponseDTO fromEntity(Indicacao indicacao) {
        return IndicacaoResponseDTO.builder()
                .id(indicacao.getId())
                .nomeInteressado(indicacao.getNomeInteressado())
                .telefone(indicacao.getTelefone())
                .idade(indicacao.getIdade())
                .bairroInteressado(indicacao.getBairroInteressado())
                .quemIndicou(indicacao.getQuemIndicou())
                .observacoes(indicacao.getObservacoes())
                .status(indicacao.getStatus())
                .dataIndicacao(indicacao.getDataIndicacao())
                .turmaId(indicacao.getTurma() != null ? indicacao.getTurma().getId() : null)
                .turmaNomeLocal(indicacao.getTurma() != null ? indicacao.getTurma().getNomeLocal() : null)
                .turmaTurno(indicacao.getTurma() != null ? indicacao.getTurma().getTurno().name() : null)
                .build();
    }
}