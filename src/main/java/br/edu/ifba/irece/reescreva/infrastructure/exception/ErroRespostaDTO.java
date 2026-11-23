package br.edu.ifba.irece.reescreva.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErroRespostaDTO(
        LocalDateTime timestamp,
        int status,
        String mensagem,
        List<String> detalhes
) {
}
