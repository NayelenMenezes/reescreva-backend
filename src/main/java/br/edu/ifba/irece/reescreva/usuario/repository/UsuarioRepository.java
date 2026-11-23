package br.edu.ifba.irece.reescreva.usuario.repository;

import br.edu.ifba.irece.reescreva.usuario.entity.PerfilUsuario;
import br.edu.ifba.irece.reescreva.usuario.entity.StatusUsuario;
import br.edu.ifba.irece.reescreva.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<Usuario> findByEmailIgnoreCase(String email);

    long countByPerfilAndStatus(PerfilUsuario perfil, StatusUsuario status);
}