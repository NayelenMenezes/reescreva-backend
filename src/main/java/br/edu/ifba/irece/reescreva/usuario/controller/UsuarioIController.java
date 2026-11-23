package br.edu.ifba.irece.reescreva.usuario.controller;

import br.edu.ifba.irece.reescreva.usuario.dto.LoginRequestDTO;
import br.edu.ifba.irece.reescreva.usuario.dto.LoginResponseDTO;
import br.edu.ifba.irece.reescreva.usuario.dto.UsuarioRequestDTO;
import br.edu.ifba.irece.reescreva.usuario.dto.UsuarioResponseDTO;
import br.edu.ifba.irece.reescreva.usuario.entity.StatusUsuario;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Contrato REST dos endpoints de Usuario.
 */
public interface UsuarioIController {

    ResponseEntity<UsuarioResponseDTO> save(UsuarioRequestDTO dto);

    ResponseEntity<List<UsuarioResponseDTO>> findAll();

    ResponseEntity<UsuarioResponseDTO> findById(Long id);

    ResponseEntity<UsuarioResponseDTO> update(Long id, UsuarioRequestDTO dto);

    ResponseEntity<UsuarioResponseDTO> updateStatus(Long id, StatusUsuario status);

    ResponseEntity<Void> delete(Long id);

    ResponseEntity<LoginResponseDTO> login(LoginRequestDTO dto);
}