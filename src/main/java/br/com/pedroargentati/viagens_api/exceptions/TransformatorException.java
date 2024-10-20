package br.com.pedroargentati.viagens_api.exceptions;

public class TransformatorException extends RuntimeException {
    public TransformatorException(String message, Throwable cause) {
        super(message, cause);
    }
}