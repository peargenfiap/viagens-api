package br.com.pedroargentati.viagens_api.exceptions;

public class FileProcessingException extends ViagensException {

    public FileProcessingException(String message) {
        super(message);
    }

    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
