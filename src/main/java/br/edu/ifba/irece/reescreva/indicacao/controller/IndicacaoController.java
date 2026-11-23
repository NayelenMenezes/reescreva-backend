package br.edu.ifba.irece.reescreva.indicacao.controller;

import br.edu.ifba.irece.reescreva.indicacao.dto.IndicacaoRequestDTO;
import br.edu.ifba.irece.reescreva.indicacao.dto.IndicacaoResponseDTO;
import br.edu.ifba.irece.reescreva.indicacao.entity.StatusIndicacao;
import br.edu.ifba.irece.reescreva.indicacao.service.IndicacaoIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para registro e acompanhamento de indicacoes de alunos.
 */
@RestController
@RequestMapping("/api/indicacoes")
@RequiredArgsConstructor
public class IndicacaoController implements IndicacaoIController {

    private final IndicacaoIService indicacaoService;

    @Override
    @PostMapping
    public ResponseEntity<IndicacaoResponseDTO> save(@RequestBody @Valid IndicacaoRequestDTO dto) {
        IndicacaoResponseDTO created = indicacaoService.save(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created); // 201 Created
    }

    @Override
    @GetMapping
    public ResponseEntity<List<IndicacaoResponseDTO>> findAll(
            @RequestParam(required = false) StatusIndicacao status,
            @RequestParam(required = false) Long turmaId
    ) {
        return ResponseEntity.ok(indicacaoService.findAll(status, turmaId)); // 200 OK
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<IndicacaoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(indicacaoService.findById(id)); // 200 OK
    }

    @Override
    @PatchMapping("/{id}/status")
    public ResponseEntity<IndicacaoResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam StatusIndicacao status
    ) {
        return ResponseEntity.ok(indicacaoService.updateStatus(id, status)); // 200 OK
    }

    @Override
    @PatchMapping("/{id}/turma/{turmaId}")
    public ResponseEntity<IndicacaoResponseDTO> assignTurma(
            @PathVariable Long id,
            @PathVariable Long turmaId
    ) {
        return ResponseEntity.ok(indicacaoService.assignTurma(id, turmaId)); // 200 OK
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        indicacaoService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}