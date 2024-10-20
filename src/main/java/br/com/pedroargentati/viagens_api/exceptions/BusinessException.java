package br.com.pedroargentati.viagens_api.exceptions;

public class BusinessException extends ViagensException {

    private String[] args;

    public BusinessException(String... message) {
        this.args = message;
    }
}
