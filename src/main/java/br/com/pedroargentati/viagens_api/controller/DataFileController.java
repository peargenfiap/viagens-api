package br.com.pedroargentati.viagens_api.controller;

import br.com.pedroargentati.viagens_api.dto.DataFileDTO;
import br.com.pedroargentati.viagens_api.exceptions.FileProcessingException;
import br.com.pedroargentati.viagens_api.exceptions.HashGenerationException;
import br.com.pedroargentati.viagens_api.service.DataFileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/datafile")
public class DataFileController {

    private final DataFileService dataFileService;

    public DataFileController(DataFileService dataFileService) {
        this.dataFileService = dataFileService;
    }

    @GetMapping("/{idFile}")
    public ResponseEntity<DataFileDTO> obterDataFilePorChave(@PathVariable String idFile) {
        var dataFile = dataFileService.obterDataFilePorChave(idFile);
        if (dataFile == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(dataFile);
    }

    @PostMapping
    public void incluirDataFile(@Valid @RequestBody DataFileDTO dto) throws HashGenerationException, FileProcessingException {
        dataFileService.incluirDataFile(dto);
    }

}
