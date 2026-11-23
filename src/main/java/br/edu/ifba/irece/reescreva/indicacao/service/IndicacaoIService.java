package br.edu.ifba.irece.reescreva.indicacao.service;

import br.edu.ifba.irece.reescreva.indicacao.dto.IndicacaoRequestDTO;
import br.edu.ifba.irece.reescreva.indicacao.dto.IndicacaoResponseDTO;
import br.edu.ifba.irece.reescreva.indicacao.entity.StatusIndicacao;

import java.util.List;

/**
 * Contrato de regras de negocio para indicacoes de alunos.
 */
public interface IndicacaoIService {

    IndicacaoResponseDTO save(IndicacaoRequestDTO dto);

    List<IndicacaoResponseDTO> findAll(StatusIndicacao status, Long turmaId);

    IndicacaoResponseDTO findById(Long id);

    IndicacaoResponseDTO updateStatus(Long id, StatusIndicacao newStatus);

    IndicacaoResponseDTO assignTurma(Long id, Long turmaId);

    void delete(Long id);
}