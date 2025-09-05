package br.com.orla.tech.api.support.exceptions;

public class RecursoExistenteException extends RuntimeException {
    public RecursoExistenteException(String message) {
        super(message);
    }
}