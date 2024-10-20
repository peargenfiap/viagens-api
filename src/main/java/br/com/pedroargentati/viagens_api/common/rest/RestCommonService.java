package br.com.pedroargentati.viagens_api.common.rest;

import br.com.pedroargentati.viagens_api.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RestCommonService {

    protected <T> ResponseEntity<T> buildResponseForPost(T entity, Object... ids) {
        if (entity == null || ids == null || ids.length == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Construir a URI com múltiplos IDs (PK composta ou simples)
        ServletUriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromCurrentRequest();

        for (Object id : ids) {
            uriBuilder.path("/{id}");
        }

        URI location = uriBuilder.buildAndExpand(ids).toUri();

        // Definir cabeçalhos
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        // Retornar a entidade e os cabeçalhos, com status HTTP 201 Created
        return new ResponseEntity<>(entity, headers, HttpStatus.CREATED);
    }

    // Método para construir uma resposta com uma entidade única
    protected <T> ResponseEntity<T> buildResponseForEntity(T entity) throws NotFoundException {
        if (entity == null) {
            throw new NotFoundException("Entidade não encontrada.");
        }
        return ResponseEntity.ok(entity);
    }

    // Método para construir uma resposta para um arquivo
    protected ResponseEntity<byte[]> buildResponseForFile(byte[] fileData, String fileName, MediaType mediaType, boolean asAttachment) {
        if (fileData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            HttpHeaders headers = new HttpHeaders();
            if (asAttachment) {
                headers.setContentDispositionFormData("attachment", fileName);
            }
            headers.setContentType(mediaType);
            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
        }
    }

    // Método para criar resposta para imagens em Base64
    protected ResponseEntity<String> buildResponseForImage(byte[] imageData, String chave) {
        if (imageData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            String base64Image = Base64.getEncoder().encodeToString(imageData);
            return ResponseEntity.ok().header("Inline", "filename=\"" + chave + "\"").body(base64Image);
        }
    }

}
