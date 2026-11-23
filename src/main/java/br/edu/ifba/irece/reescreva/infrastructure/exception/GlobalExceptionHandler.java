package br.edu.ifba.irece.reescreva.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

/**Intercepta excecoes da aplicacao e converte em respostas HTTP padronizadas*/
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroRespostaDTO> tratarRegraDeNegocio(RegraDeNegocioException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroRespostaDTO> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ErroRespostaDTO> tratarCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRespostaDTO> tratarValidacao(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();

        ErroRespostaDTO erro = new ErroRespostaDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validacao nos dados enviados.",
                detalhes
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroRespostaDTO> tratarJsonMalformado(HttpMessageNotReadableException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Corpo da requisicao ausente ou malformado.",
                List.of()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}