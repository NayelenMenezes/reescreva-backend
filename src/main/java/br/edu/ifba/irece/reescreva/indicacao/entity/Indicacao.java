package br.edu.ifba.irece.reescreva.indicacao.entity;

import br.edu.ifba.irece.reescreva.turma.entity.Turma;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade JPA que registra o interesse de um jovem, adulto ou idoso em participar de uma turma.
 */
@Entity
@Table(name = "indicacoes")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Indicacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome_interessado", nullable = false, length = 150)
    private String nomeInteressado;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(nullable = false)
    private Integer idade;

    @Column(name = "bairro_interessado", length = 100)
    private String bairroInteressado;

    @Column(name = "quem_indicou", length = 150)
    private String quemIndicou;

    @Column(length = 500)
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusIndicacao status = StatusIndicacao.PENDENTE;

    @Column(name = "data_indicacao", nullable = false)
    @Builder.Default
    private LocalDateTime dataIndicacao = LocalDateTime.now();

    // Relacionamento N:1 opcional (o interessado pode escolher uma turma no formulario ou aguardar orientacao)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id")
    private Turma turma;
}