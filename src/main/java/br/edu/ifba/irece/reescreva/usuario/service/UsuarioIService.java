package br.edu.ifba.irece.reescreva.usuario.service;

import br.edu.ifba.irece.reescreva.usuario.dto.LoginRequestDTO;
import br.edu.ifba.irece.reescreva.usuario.dto.LoginResponseDTO;
import br.edu.ifba.irece.reescreva.usuario.dto.UsuarioRequestDTO;
import br.edu.ifba.irece.reescreva.usuario.dto.UsuarioResponseDTO;
import br.edu.ifba.irece.reescreva.usuario.entity.StatusUsuario;

import java.util.List;

/**
 * Contrato de regras de negocio para gerenciamento de usuarios.
 */
public interface UsuarioIService {

    UsuarioResponseDTO save(UsuarioRequestDTO dto);

    List<UsuarioResponseDTO> findAll();

    UsuarioResponseDTO findById(Long id);

    UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto);

    UsuarioResponseDTO updateStatus(Long id, StatusUsuario newStatus);

    void delete(Long id);

    LoginResponseDTO login(LoginRequestDTO dto);
}