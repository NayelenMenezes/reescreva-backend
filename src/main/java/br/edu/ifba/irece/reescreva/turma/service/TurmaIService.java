package br.edu.ifba.irece.reescreva.turma.service;

import br.edu.ifba.irece.reescreva.turma.dto.TurmaResponseDTO;
import br.edu.ifba.irece.reescreva.turma.dto.TurmaRequestDTO;
import br.edu.ifba.irece.reescreva.turma.entity.Turno;

import java.util.List;

/**
 * Contrato de regras de negocio para gerenciamento de turmas e pontos de ensino.
 */
public interface TurmaIService {

    TurmaResponseDTO save(TurmaRequestDTO dto);

    List<TurmaResponseDTO> findAll(Turno turno, String bairro, boolean onlyActive);

    TurmaResponseDTO findById(Long id);

    TurmaResponseDTO update(Long id, TurmaRequestDTO dto);

    TurmaResponseDTO updateStatus(Long id, boolean ativa);

    void delete(Long id);
}