package br.edu.ifba.irece.reescreva.infrastructure.config;

import br.edu.ifba.irece.reescreva.usuario.entity.PerfilUsuario;
import br.edu.ifba.irece.reescreva.usuario.entity.StatusUsuario;
import br.edu.ifba.irece.reescreva.usuario.entity.Usuario;
import br.edu.ifba.irece.reescreva.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Popula o banco com um administrador padrao na primeira execucao, caso esteja vazio.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            Usuario admin = Usuario.builder()
                    .nomeCompleto("Administrador Reescreva")
                    .email("admin@reescreva.com")
                    .senha("admin123")
                    .perfil(PerfilUsuario.ADMINISTRADOR)
                    .status(StatusUsuario.ATIVO)
                    .build();

            usuarioRepository.save(admin);
            System.out.println(">>> Administrador padrao criado: admin@reescreva.com / admin123");
        }
    }
}