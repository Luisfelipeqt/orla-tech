package br.com.orla.tech.api.support.exceptions;

public class ClientException extends RuntimeException {
    public ClientException(String mensagem) {
        super(mensagem);
    }
}
