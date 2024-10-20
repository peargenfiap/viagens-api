package br.com.pedroargentati.viagens_api.controller;

import br.com.pedroargentati.viagens_api.common.rest.RestCommonService;
import br.com.pedroargentati.viagens_api.dto.DataFileDTO;
import br.com.pedroargentati.viagens_api.exceptions.FileProcessingException;
import br.com.pedroargentati.viagens_api.exceptions.HashGenerationException;
import br.com.pedroargentati.viagens_api.exceptions.NotFoundException;
import br.com.pedroargentati.viagens_api.service.DataFileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/datafile")
public class DataFileController extends RestCommonService {

    private final DataFileService dataFileService;

    public DataFileController(DataFileService dataFileService) {
        this.dataFileService = dataFileService;
    }

    @GetMapping("/{idFile}")
    public ResponseEntity<DataFileDTO> obterDataFilePorChave(@PathVariable String idFile) throws NotFoundException {
        var dataFile = dataFileService.obterDataFilePorChave(idFile);

        return super.buildResponseForEntity(dataFile);
    }

    @PostMapping
    public ResponseEntity<DataFileDTO> incluirDataFile(@Valid @RequestBody DataFileDTO dto) throws HashGenerationException, FileProcessingException {
        dataFileService.incluirDataFile(dto);
        return super.buildResponseForPost(dto, dto.idFile());
    }

}
