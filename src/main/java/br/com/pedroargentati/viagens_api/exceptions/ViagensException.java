package br.com.pedroargentati.viagens_api.exceptions;

public class ViagensException extends Exception {

    public ViagensException() {
        super();
    }

    public ViagensException(String message) {
        super(message);
    }

    public ViagensException(String message, Throwable cause) {
        super(message, cause);
    }

}
