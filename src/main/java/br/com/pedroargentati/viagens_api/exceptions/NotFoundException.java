package br.com.pedroargentati.viagens_api.exceptions;

public class NotFoundException extends ViagensException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
