package br.edu.ifba.irece.reescreva.indicacao.repository;

import br.edu.ifba.irece.reescreva.indicacao.entity.Indicacao;
import br.edu.ifba.irece.reescreva.indicacao.entity.StatusIndicacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndicacaoRepository extends JpaRepository<Indicacao, Long> {

    boolean existsByTurmaId(Long turmaId);

    boolean existsByTelefoneAndStatusIn(String telefone, List<StatusIndicacao> statuses);

    List<Indicacao> findByStatus(StatusIndicacao status);

    List<Indicacao> findByTurmaId(Long turmaId);

    List<Indicacao> findByStatusAndTurmaId(StatusIndicacao status, Long turmaId);
}