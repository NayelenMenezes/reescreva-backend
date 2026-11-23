package br.edu.ifba.irece.reescreva.indicacao.controller;

import br.edu.ifba.irece.reescreva.indicacao.dto.IndicacaoRequestDTO;
import br.edu.ifba.irece.reescreva.indicacao.dto.IndicacaoResponseDTO;
import br.edu.ifba.irece.reescreva.indicacao.entity.StatusIndicacao;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Contrato REST dos endpoints de Indicacao.
 */
public interface IndicacaoIController {

    ResponseEntity<IndicacaoResponseDTO> save(IndicacaoRequestDTO dto);

    ResponseEntity<List<IndicacaoResponseDTO>> findAll(StatusIndicacao status, Long turmaId);

    ResponseEntity<IndicacaoResponseDTO> findById(Long id);

    ResponseEntity<IndicacaoResponseDTO> updateStatus(Long id, StatusIndicacao status);

    ResponseEntity<IndicacaoResponseDTO> assignTurma(Long id, Long turmaId);

    ResponseEntity<Void> delete(Long id);
}