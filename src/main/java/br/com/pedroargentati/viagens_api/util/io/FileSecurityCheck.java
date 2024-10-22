package br.com.pedroargentati.viagens_api.util.io;

import br.com.pedroargentati.viagens_api.exceptions.BusinessException;
import br.com.pedroargentati.viagens_api.exceptions.ViagensException;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class FileSecurityCheck {

    private static final Logger logger = Logger.getLogger(FileSecurityCheck.class.getName());

    @Value("${max.file.size:262144000}") // 250 * 1024 * 1024 (em bytes)
    private int MAX_FILE_SIZE;

    @Value("${min.file.size:1024}")
    private int MIN_FILE_SIZE;

    @Value("${max.file.name.length:255}")
    private int MAX_FILE_NAME_LENGTH;

    @Value("${min.file.name.length:3}")
    private int MIN_FILE_NAME_LENGTH;


    public final void check(String fileName, String mimeType, InputStream dataStream) throws ViagensException, IOException {
        // Verificações de segurança
        try {
            this.validateName(fileName);

            if (mimeType == null || mimeType.trim().isEmpty()) {
                throw new BusinessException("error.file.mime.required");
            }
            if (dataStream == null || dataStream.available() == 0) {
                throw new BusinessException("error.file.content.required");
            }

        } catch (BusinessException be) {
            logger.info(be.toString());
            throw be;
        }
    }

    public final void validateName(String fileName) {
        try {
            if (fileName == null || fileName.trim().isEmpty()) {
                throw new BusinessException("É necessário informar o nome do arquivo.");
            }

            if (!fileName.contains(".") || fileName.indexOf(".") != fileName.lastIndexOf(".")) {
                throw new BusinessException("error.file.extension.invalid");
            }

            String name = fileName.substring(0, fileName.indexOf(".")).trim();
            if (name.length() < this.MIN_FILE_NAME_LENGTH || name.length() > this.MAX_FILE_NAME_LENGTH) {
                throw new BusinessException("Erro ! Arquivo com nome inválido.", fileName, String.valueOf(name.length()), String.valueOf(this.MIN_FILE_NAME_LENGTH), String.valueOf(this.MAX_FILE_NAME_LENGTH));
            }

            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                if (c >= 48 && c <= 57) {
                    continue;
                }

                if (c >= 65 && c <= 90) {
                    continue;
                }

                if (c >= 97 && c <= 122) {
                    continue;
                }

                if (c >= 161 && c <= 255) {
                    continue;
                }

                if (c == 32 || c == 45 || c == 95) {
                    continue;
                }
                throw new BusinessException("Erro ! O arquivo contém caracteres inválidos.", fileName, String.valueOf(c));
            }

        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.indexOf(".") + 1)
                .trim()
                .toLowerCase();
    }

}
