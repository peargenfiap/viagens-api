package br.com.pedroargentati.viagens_api.exceptions;

public class RecordNotFoundException extends ViagensException {

    public RecordNotFoundException() {
        super();
    }

    public RecordNotFoundException(String message) {
        super(message);
    }

    public RecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
