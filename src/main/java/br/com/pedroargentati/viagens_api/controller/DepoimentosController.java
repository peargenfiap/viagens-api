package br.com.pedroargentati.viagens_api.controller;

import br.com.pedroargentati.viagens_api.common.rest.RestCommonService;
import br.com.pedroargentati.viagens_api.dto.DepoimentosDTO;
import br.com.pedroargentati.viagens_api.exceptions.RecordNotFoundException;
import br.com.pedroargentati.viagens_api.model.Depoimentos;
import br.com.pedroargentati.viagens_api.service.DepoimentosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/depoimentos")
public class DepoimentosController extends RestCommonService {

    private final DepoimentosService depoimentosService;

    @Autowired
    public DepoimentosController(DepoimentosService depoimentosService) {
        this.depoimentosService = depoimentosService;
    }

    @GetMapping
    public Page<DepoimentosDTO> obterDepoimentos(@PageableDefault(size = 15) Pageable pageable) {
        return depoimentosService.obterListaDepoimentos(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepoimentosDTO> obterDepoimentoPorId(@PathVariable Integer id) throws RecordNotFoundException {
        return super.buildResponseForEntity(depoimentosService.obterDepoimentoPorId(id));
    }

    @PostMapping
    public ResponseEntity<DepoimentosDTO> incluirDepoimento(@RequestBody DepoimentosDTO dto) throws RecordNotFoundException {
        depoimentosService.incluirDepoimento(dto);
        return super.buildResponseForPost(dto, dto.id());
    }

    @PutMapping
    public ResponseEntity<DepoimentosDTO> atualizarDepoimento(@RequestBody DepoimentosDTO dto) throws RecordNotFoundException {
        depoimentosService.atualizarDepoimento(dto);
        return super.buildResponseForPost(dto, dto.id());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Depoimentos> excluirDepoimento(@PathVariable Integer id) throws RecordNotFoundException {
        var depoimento = depoimentosService.excluirDepoimento(id);
        return super.buildResponseForDelete(depoimento);
    }

}
