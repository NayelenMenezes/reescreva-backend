package br.edu.ifba.irece.reescreva.turma.controller;

import br.edu.ifba.irece.reescreva.turma.dto.TurmaRequestDTO;
import br.edu.ifba.irece.reescreva.turma.dto.TurmaResponseDTO;
import br.edu.ifba.irece.reescreva.turma.entity.Turno;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Contrato REST dos endpoints de Turma.
 */
public interface TurmaIController {

    ResponseEntity<TurmaResponseDTO> save(TurmaRequestDTO dto);

    ResponseEntity<List<TurmaResponseDTO>> findAll(Turno turno, String bairro, boolean onlyActive);

    ResponseEntity<TurmaResponseDTO> findById(Long id);

    ResponseEntity<TurmaResponseDTO> update(Long id, TurmaRequestDTO dto);

    ResponseEntity<TurmaResponseDTO> updateStatus(Long id, boolean ativa);

    ResponseEntity<Void> delete(Long id);
}