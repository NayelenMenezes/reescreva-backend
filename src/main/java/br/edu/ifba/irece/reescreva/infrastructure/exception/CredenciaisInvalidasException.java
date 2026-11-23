package br.edu.ifba.irece.reescreva.infrastructure.exception;

/**Excecao lancada quando o login falha por senha incorreta ou conta inativa ->HTTP 401*/
public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException(String mensagem) {
        super(mensagem);
    }
}