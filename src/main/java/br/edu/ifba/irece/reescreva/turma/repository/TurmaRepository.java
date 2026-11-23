package br.edu.ifba.irece.reescreva.turma.repository;

import br.edu.ifba.irece.reescreva.turma.entity.Turma;
import br.edu.ifba.irece.reescreva.turma.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurmaRepository extends JpaRepository<Turma, Long> {

    boolean existsByNomeLocalIgnoreCaseAndTurno(String nomeLocal, Turno turno);

    List<Turma> findByAtivaTrue();

    List<Turma> findByTurno(Turno turno);

    List<Turma> findByTurnoAndAtivaTrue(Turno turno);

    List<Turma> findByBairroContainingIgnoreCase(String bairro);

    List<Turma> findByBairroContainingIgnoreCaseAndAtivaTrue(String bairro);

    List<Turma> findByTurnoAndBairroContainingIgnoreCase(Turno turno, String bairro);

    List<Turma> findByTurnoAndBairroContainingIgnoreCaseAndAtivaTrue(Turno turno, String bairro);
}