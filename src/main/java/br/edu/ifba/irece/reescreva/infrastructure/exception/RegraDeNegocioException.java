package br.edu.ifba.irece.reescreva.infrastructure.exception;

/**Excecao lancada quando uma regra de negocio é violada*/
public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String mensagem){
        super(mensagem);
    }
}
