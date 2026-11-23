package br.edu.ifba.irece.reescreva.turma.service;

import br.edu.ifba.irece.reescreva.indicacao.repository.IndicacaoRepository;
import br.edu.ifba.irece.reescreva.infrastructure.exception.RecursoNaoEncontradoException;
import br.edu.ifba.irece.reescreva.infrastructure.exception.RegraDeNegocioException;
import br.edu.ifba.irece.reescreva.turma.dto.TurmaResponseDTO;
import br.edu.ifba.irece.reescreva.turma.dto.TurmaRequestDTO;
import br.edu.ifba.irece.reescreva.turma.entity.Turma;
import br.edu.ifba.irece.reescreva.turma.entity.Turno;
import br.edu.ifba.irece.reescreva.turma.repository.TurmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementacao das regras de negocio de Turma.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TurmaService implements TurmaIService {

    private final TurmaRepository turmaRepository;
    private final IndicacaoRepository indicacaoRepository;

    @Override
    @Transactional
    public TurmaResponseDTO save(TurmaRequestDTO dto) {
        String localNormalizado = dto.nomeLocal().trim();

        // Regra 1: Impede cadastrar duas turmas no mesmo local e no mesmo turno
        if (turmaRepository.existsByNomeLocalIgnoreCaseAndTurno(localNormalizado, dto.turno())) {
            throw new RegraDeNegocioException(
                    "Ja existe uma turma cadastrada neste local para o turno " + dto.turno() + "."
            );
        }

        // Regra 2: Valida limites geograficos caso latitude/longitude sejam informadas para o mapa
        validateCoordinates(dto.latitude(), dto.longitude());

        Turma novaTurma = Turma.builder()
                .nomeLocal(localNormalizado)
                .bairro(dto.bairro().trim())
                .endereco(dto.endereco().trim())
                .turno(dto.turno())
                .vagasTotais(dto.vagasTotais())
                .vagasOcupadas(0)
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .ativa(dto.ativa() != null ? dto.ativa() : true)
                .build();

        return TurmaResponseDTO.fromEntity(turmaRepository.save(novaTurma));
    }

    @Override
    public List<TurmaResponseDTO> findAll(Turno turno, String bairro, boolean onlyActive) {
        boolean hasTurno = turno != null;
        boolean hasBairro = bairro != null && !bairro.isBlank();
        String bairroFiltro = hasBairro ? bairro.trim() : null;

        List<Turma> turmas;

        if (hasTurno && hasBairro) {
            turmas = onlyActive
                    ? turmaRepository.findByTurnoAndBairroContainingIgnoreCaseAndAtivaTrue(turno, bairroFiltro)
                    : turmaRepository.findByTurnoAndBairroContainingIgnoreCase(turno, bairroFiltro);
        } else if (hasTurno) {
            turmas = onlyActive
                    ? turmaRepository.findByTurnoAndAtivaTrue(turno)
                    : turmaRepository.findByTurno(turno);
        } else if (hasBairro) {
            turmas = onlyActive
                    ? turmaRepository.findByBairroContainingIgnoreCaseAndAtivaTrue(bairroFiltro)
                    : turmaRepository.findByBairroContainingIgnoreCase(bairroFiltro);
        } else {
            turmas = onlyActive
                    ? turmaRepository.findByAtivaTrue()
                    : turmaRepository.findAll();
        }

        return turmas.stream()
                .map(TurmaResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public TurmaResponseDTO findById(Long id) {
        return TurmaResponseDTO.fromEntity(findEntityById(id));
    }

    @Override
    @Transactional
    public TurmaResponseDTO update(Long id, TurmaRequestDTO dto) {
        Turma turma = findEntityById(id);
        String localNormalizado = dto.nomeLocal().trim();

        // Regra 1: Se alterou local ou turno, verifica duplicidade
        boolean mudouChaveUnica = !turma.getNomeLocal().equalsIgnoreCase(localNormalizado)
                || turma.getTurno() != dto.turno();

        if (mudouChaveUnica && turmaRepository.existsByNomeLocalIgnoreCaseAndTurno(localNormalizado, dto.turno())) {
            throw new RegraDeNegocioException(
                    "Ja existe outra turma cadastrada neste local para o turno " + dto.turno() + "."
            );
        }

        // Regra 2: Valida coordenadas do mapa
        validateCoordinates(dto.latitude(), dto.longitude());

        // Regra 3: Nao permite reduzir o total de vagas para menos do que ja esta ocupado
        if (dto.vagasTotais() < turma.getVagasOcupadas()) {
            throw new RegraDeNegocioException(
                    "O total de vagas (" + dto.vagasTotais() + ") nao pode ser menor que o numero de alunos ja matriculados (" + turma.getVagasOcupadas() + ")."
            );
        }

        turma.setNomeLocal(localNormalizado);
        turma.setBairro(dto.bairro().trim());
        turma.setEndereco(dto.endereco().trim());
        turma.setTurno(dto.turno());
        turma.setVagasTotais(dto.vagasTotais());
        turma.setLatitude(dto.latitude());
        turma.setLongitude(dto.longitude());
        if (dto.ativa() != null) {
            turma.setAtiva(dto.ativa());
        }

        return TurmaResponseDTO.fromEntity(turmaRepository.save(turma));
    }

    @Override
    @Transactional
    public TurmaResponseDTO updateStatus(Long id, boolean ativa) {
        Turma turma = findEntityById(id);
        turma.setAtiva(ativa);
        return TurmaResponseDTO.fromEntity(turmaRepository.save(turma));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Turma turma = findEntityById(id);

        // Regra 4: Impede exclusao fisica de turma que ja possui indicacoes vinculadas
        if (indicacaoRepository.existsByTurmaId(id)) {
            throw new RegraDeNegocioException(
                    "Nao e possivel excluir esta turma pois existem indicacoes vinculadas a ela. Desative a turma em vez de excluir."
            );
        }

        turmaRepository.delete(turma);
    }

    private Turma findEntityById(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Turma nao encontrada com o ID: " + id));
    }

    private void validateCoordinates(Double latitude, Double longitude) {
        if (latitude != null && (latitude < -90.0 || latitude > 90.0)) {
            throw new RegraDeNegocioException("Latitude invalida: deve estar entre -90 e 90 graus.");
        }
        if (longitude != null && (longitude < -180.0 || longitude > 180.0)) {
            throw new RegraDeNegocioException("Longitude invalida: deve estar entre -180 e 180 graus.");
        }
    }
}