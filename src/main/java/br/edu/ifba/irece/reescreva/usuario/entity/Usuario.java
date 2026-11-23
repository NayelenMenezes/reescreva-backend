package br.edu.ifba.irece.reescreva.usuario.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade JPA que representa um usuario administrativo ou coordenador do Reescreva.
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome_completo", nullable = false, length = 150)
    private String nomeCompleto;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private PerfilUsuario perfil = PerfilUsuario.COORDENADOR;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusUsuario status = StatusUsuario.ATIVO;

    // Metodos de dominio para encapsular mudancas de estado
    public void ativar() {
        this.status = StatusUsuario.ATIVO;
    }

    public void desativar() {
        this.status = StatusUsuario.INATIVO;
    }

    public boolean isAtivo() {
        return this.status == StatusUsuario.ATIVO;
    }

    public boolean isAdministrador() {
        return this.perfil == PerfilUsuario.ADMINISTRADOR;
    }
}