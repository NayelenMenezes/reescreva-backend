package br.edu.ifba.irece.reescreva.indicacao.service;

import br.edu.ifba.irece.reescreva.indicacao.dto.IndicacaoRequestDTO;
import br.edu.ifba.irece.reescreva.indicacao.dto.IndicacaoResponseDTO;
import br.edu.ifba.irece.reescreva.indicacao.entity.Indicacao;
import br.edu.ifba.irece.reescreva.indicacao.entity.StatusIndicacao;
import br.edu.ifba.irece.reescreva.indicacao.repository.IndicacaoRepository;
import br.edu.ifba.irece.reescreva.infrastructure.exception.RecursoNaoEncontradoException;
import br.edu.ifba.irece.reescreva.infrastructure.exception.RegraDeNegocioException;
import br.edu.ifba.irece.reescreva.turma.entity.Turma;
import br.edu.ifba.irece.reescreva.turma.repository.TurmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementacao das regras de negocio de Indicacao.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IndicacaoService implements IndicacaoIService {

    private final IndicacaoRepository indicacaoRepository;
    private final TurmaRepository turmaRepository;

    @Override
    @Transactional
    public IndicacaoResponseDTO save(IndicacaoRequestDTO dto) {
        // Regra 1: Validacao do publico-alvo (EJA / Alfabetizacao exige idade minima de 15 anos)
        if (dto.idade() < 15 || dto.idade() > 120) {
            throw new RegraDeNegocioException(
                    "Idade invalida: o programa EJA/alfabetizacao atende jovens, adultos e idosos a partir de 15 anos."
            );
        }

        // Regra 2: Normalizacao de telefone e bloqueio de duplicidade em atendimento ativo
        String telefoneLimpo = dto.telefone().replaceAll("\\D", "");
        if (telefoneLimpo.length() < 10 || telefoneLimpo.length() > 11) {
            throw new RegraDeNegocioException("Telefone invalido: informe DDD + numero com 10 ou 11 digitos.");
        }

        List<StatusIndicacao> statusAtivos = List.of(
                StatusIndicacao.PENDENTE,
                StatusIndicacao.EM_CONTATO,
                StatusIndicacao.MATRICULADO
        );

        if (indicacaoRepository.existsByTelefoneAndStatusIn(telefoneLimpo, statusAtivos)) {
            throw new RegraDeNegocioException(
                    "Ja existe uma indicacao ativa ou matricula registrada para este telefone."
            );
        }

        // Regra 3: Se uma turma foi escolhida no formulario, valida existencia, status ativo e vagas
        Turma turmaSelecionada = null;
        if (dto.turmaId() != null) {
            turmaSelecionada = turmaRepository.findById(dto.turmaId())
                    .orElseThrow(() -> new RegraDeNegocioException("A turma informada nao foi encontrada."));

            if (!Boolean.TRUE.equals(turmaSelecionada.getAtiva())) {
                throw new RegraDeNegocioException("A turma selecionada esta inativa no momento.");
            }

            if (!turmaSelecionada.hasVagasDisponiveis()) {
                throw new RegraDeNegocioException("A turma selecionada nao possui mais vagas disponiveis.");
            }
        }

        Indicacao novaIndicacao = Indicacao.builder()
                .nomeInteressado(dto.nomeInteressado().trim())
                .telefone(telefoneLimpo)
                .idade(dto.idade())
                .bairroInteressado(dto.bairroInteressado() != null ? dto.bairroInteressado().trim() : null)
                .quemIndicou(dto.quemIndicou() != null && !dto.quemIndicou().isBlank()
                        ? dto.quemIndicou().trim()
                        : "Proprio interessado")
                .observacoes(dto.observacoes() != null ? dto.observacoes().trim() : null)
                .status(StatusIndicacao.PENDENTE)
                .dataIndicacao(LocalDateTime.now())
                .turma(turmaSelecionada)
                .build();

        return IndicacaoResponseDTO.fromEntity(indicacaoRepository.save(novaIndicacao));
    }

    @Override
    public List<IndicacaoResponseDTO> findAll(StatusIndicacao status, Long turmaId) {
        List<Indicacao> lista;

        if (status != null && turmaId != null) {
            lista = indicacaoRepository.findByStatusAndTurmaId(status, turmaId);
        } else if (status != null) {
            lista = indicacaoRepository.findByStatus(status);
        } else if (turmaId != null) {
            lista = indicacaoRepository.findByTurmaId(turmaId);
        } else {
            lista = indicacaoRepository.findAll();
        }

        return lista.stream()
                .map(IndicacaoResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public IndicacaoResponseDTO findById(Long id) {
        return IndicacaoResponseDTO.fromEntity(findEntityById(id));
    }

    @Override
    @Transactional
    public IndicacaoResponseDTO updateStatus(Long id, StatusIndicacao newStatus) {
        Indicacao indicacao = findEntityById(id);
        StatusIndicacao statusAnterior = indicacao.getStatus();

        if (statusAnterior == newStatus) {
            return IndicacaoResponseDTO.fromEntity(indicacao);
        }

        // Regra 4: Ao confirmar MATRICULADO, exige turma vinculada e consome 1 vaga da turma
        if (newStatus == StatusIndicacao.MATRICULADO) {
            Turma turma = indicacao.getTurma();
            if (turma == null) {
                throw new RegraDeNegocioException(
                        "Nao e possivel matricular o interessado sem antes vincular uma turma."
                );
            }
            if (!turma.hasVagasDisponiveis()) {
                throw new RegraDeNegocioException(
                        "A turma vinculada nao possui vagas disponiveis para efetivar a matricula."
                );
            }
            turma.ocuparVaga();
            turmaRepository.save(turma);
        }

        // Regra 5: Se estava MATRICULADO e mudou para outro status (ex: CANCELADO), devolve 1 vaga a turma
        if (statusAnterior == StatusIndicacao.MATRICULADO && newStatus != StatusIndicacao.MATRICULADO) {
            Turma turma = indicacao.getTurma();
            if (turma != null) {
                turma.liberarVaga();
                turmaRepository.save(turma);
            }
        }

        indicacao.setStatus(newStatus);
        return IndicacaoResponseDTO.fromEntity(indicacaoRepository.save(indicacao));
    }

    @Override
    @Transactional
    public IndicacaoResponseDTO assignTurma(Long id, Long turmaId) {
        Indicacao indicacao = findEntityById(id);

        if (indicacao.getStatus() == StatusIndicacao.MATRICULADO) {
            throw new RegraDeNegocioException(
                    "Nao e possivel trocar a turma de um aluno ja matriculado. Cancele a matricula atual primeiro."
            );
        }

        Turma novaTurma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Turma nao encontrada com o ID: " + turmaId));

        if (!novaTurma.hasVagasDisponiveis()) {
            throw new RegraDeNegocioException("A turma escolhida esta inativa ou sem vagas disponiveis.");
        }

        indicacao.setTurma(novaTurma);
        return IndicacaoResponseDTO.fromEntity(indicacaoRepository.save(indicacao));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Indicacao indicacao = findEntityById(id);

        // Regra 5: Se excluir uma indicacao ja matriculada, libera a vaga ocupada na turma
        if (indicacao.getStatus() == StatusIndicacao.MATRICULADO && indicacao.getTurma() != null) {
            Turma turma = indicacao.getTurma();
            turma.liberarVaga();
            turmaRepository.save(turma);
        }

        indicacaoRepository.delete(indicacao);
    }

    private Indicacao findEntityById(Long id) {
        return indicacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Indicacao nao encontrada com o ID: " + id));
    }
}