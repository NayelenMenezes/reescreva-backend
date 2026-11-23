package br.edu.ifba.irece.reescreva.turma.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade JPA que representa um ponto de ensino / turma disponivel na regiao.
 */
@Entity
@Table(name = "turmas")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome_local", nullable = false, length = 150)
    private String nomeLocal;

    @Column(nullable = false, length = 100)
    private String bairro;

    @Column(nullable = false, length = 255)
    private String endereco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Turno turno;

    @Column(name = "vagas_totais", nullable = false)
    private Integer vagasTotais;

    @Column(name = "vagas_ocupadas", nullable = false)
    @Builder.Default
    private Integer vagasOcupadas = 0;

    // Coordenadas geograficas para exibicao dos marcadores no mapa
    private Double latitude;

    private Double longitude;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativa = true;

    // Metodos de dominio para controle de vagas e estado
    public int getVagasDisponiveis() {
        return Math.max(0, this.vagasTotais - this.vagasOcupadas);
    }

    public boolean hasVagasDisponiveis() {
        return Boolean.TRUE.equals(this.ativa) && this.vagasOcupadas < this.vagasTotais;
    }

    public void ocuparVaga() {
        if (!hasVagasDisponiveis()) {
            throw new IllegalStateException("Nao ha vagas disponiveis nesta turma.");
        }
        this.vagasOcupadas++;
    }

    public void liberarVaga() {
        if (this.vagasOcupadas > 0) {
            this.vagasOcupadas--;
        }
    }
}