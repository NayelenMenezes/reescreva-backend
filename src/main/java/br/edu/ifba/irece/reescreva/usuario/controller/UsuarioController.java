package br.edu.ifba.irece.reescreva.usuario.controller;

import br.edu.ifba.irece.reescreva.usuario.dto.LoginRequestDTO;
import br.edu.ifba.irece.reescreva.usuario.dto.LoginResponseDTO;
import br.edu.ifba.irece.reescreva.usuario.dto.UsuarioRequestDTO;
import br.edu.ifba.irece.reescreva.usuario.dto.UsuarioResponseDTO;
import br.edu.ifba.irece.reescreva.usuario.entity.StatusUsuario;
import br.edu.ifba.irece.reescreva.usuario.service.UsuarioIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST responsavel pelas operacoes de Usuarios e Autenticacao.
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController implements UsuarioIController {

    private final UsuarioIService usuarioService;

    @Override
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> save(@RequestBody @Valid UsuarioRequestDTO dto) {
        UsuarioResponseDTO created = usuarioService.save(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created); // 201 Created
    }

    @Override
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll()); // 200 OK
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id)); // 200 OK
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioRequestDTO dto
    ) {
        return ResponseEntity.ok(usuarioService.update(id, dto)); // 200 OK
    }

    @Override
    @PatchMapping("/{id}/status")
    public ResponseEntity<UsuarioResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam StatusUsuario status
    ) {
        return ResponseEntity.ok(usuarioService.updateStatus(id, status)); // 200 OK
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.login(dto)); // 200 OK
    }
}