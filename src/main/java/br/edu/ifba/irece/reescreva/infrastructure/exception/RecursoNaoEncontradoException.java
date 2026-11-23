package br.edu.ifba.irece.reescreva.infrastructure.exception;

/**Excecao lancada quando um registro solicitado nao existe no banco -> HTTP 404*/
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
