package br.com.pedroargentati.viagens_api.util.crypto;

import br.com.pedroargentati.viagens_api.exceptions.HashGenerationException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    private HashUtil() {
        // Construtor privado para evitar instânciação da classe
    }

    public static String calcularHash(byte[] data, String algoritmo) throws HashGenerationException {
        try {
            MessageDigest digest = MessageDigest.getInstance(algoritmo);
            digest.reset();
            digest.update(data);
            byte[] idFileInBytes = digest.digest();

            return String.format("%040x", new java.math.BigInteger(1, idFileInBytes));
        } catch (NoSuchAlgorithmException e) {
            throw new HashGenerationException("Erro ao gerar o hash com o algoritmo " + algoritmo);
        }
    }
}
