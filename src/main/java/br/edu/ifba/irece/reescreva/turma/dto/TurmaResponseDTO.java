package br.edu.ifba.irece.reescreva.turma.dto;

import br.edu.ifba.irece.reescreva.turma.entity.Turma;
import br.edu.ifba.irece.reescreva.turma.entity.Turno;
import lombok.Builder;

/**
 * Dados retornados pela API para exibicao na lista e nos marcadores do mapa.
 */
@Builder
public record TurmaResponseDTO(
        Long id,
        String nomeLocal,
        String bairro,
        String endereco,
        Turno turno,
        Integer vagasTotais,
        Integer vagasOcupadas,
        Integer vagasDisponiveis,
        Double latitude,
        Double longitude,
        Boolean ativa
) {
    public static TurmaResponseDTO fromEntity(Turma turma) {
        return TurmaResponseDTO.builder()
                .id(turma.getId())
                .nomeLocal(turma.getNomeLocal())
                .bairro(turma.getBairro())
                .endereco(turma.getEndereco())
                .turno(turma.getTurno())
                .vagasTotais(turma.getVagasTotais())
                .vagasOcupadas(turma.getVagasOcupadas())
                .vagasDisponiveis(turma.getVagasDisponiveis())
                .latitude(turma.getLatitude())
                .longitude(turma.getLongitude())
                .ativa(turma.getAtiva())
                .build();
    }
}