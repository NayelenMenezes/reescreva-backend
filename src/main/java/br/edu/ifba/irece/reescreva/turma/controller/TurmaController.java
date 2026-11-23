package br.edu.ifba.irece.reescreva.turma.controller;

import br.edu.ifba.irece.reescreva.turma.dto.TurmaResponseDTO;
import br.edu.ifba.irece.reescreva.turma.dto.TurmaRequestDTO;
import br.edu.ifba.irece.reescreva.turma.entity.Turno;
import br.edu.ifba.irece.reescreva.turma.service.TurmaIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para consulta, filtragem e gestao de Turmas / Pontos de Ensino.
 */
@RestController
@RequestMapping("/api/turmas")
@RequiredArgsConstructor
public class TurmaController implements TurmaIController {

    private final TurmaIService turmaService;

    @Override
    @PostMapping
    public ResponseEntity<TurmaResponseDTO> save(@RequestBody @Valid TurmaRequestDTO dto) {
        TurmaResponseDTO created = turmaService.save(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created); // 201 Created
    }

    @Override
    @GetMapping
    public ResponseEntity<List<TurmaResponseDTO>> findAll(
            @RequestParam(required = false) Turno turno,
            @RequestParam(required = false) String bairro,
            @RequestParam(defaultValue = "false") boolean onlyActive
    ) {
        return ResponseEntity.ok(turmaService.findAll(turno, bairro, onlyActive)); // 200 OK
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TurmaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(turmaService.findById(id)); // 200 OK
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<TurmaResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid TurmaRequestDTO dto
    ) {
        return ResponseEntity.ok(turmaService.update(id, dto)); // 200 OK
    }

    @Override
    @PatchMapping("/{id}/status")
    public ResponseEntity<TurmaResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam boolean ativa
    ) {
        return ResponseEntity.ok(turmaService.updateStatus(id, ativa)); // 200 OK
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        turmaService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}