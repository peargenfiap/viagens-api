package br.com.pedroargentati.viagens_api.controller;

import br.com.pedroargentati.viagens_api.common.rest.RestCommonService;
import br.com.pedroargentati.viagens_api.dto.DepoimentosDTO;
import br.com.pedroargentati.viagens_api.service.DepoimentosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
