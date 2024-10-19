package br.com.pedroargentati.viagens_api.service;

import br.com.pedroargentati.viagens_api.dto.DepoimentosDTO;
import br.com.pedroargentati.viagens_api.repository.DepoimentosRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DepoimentosService {

    private final DepoimentosRepository depoimentosRepository;

    public DepoimentosService(DepoimentosRepository depoimentosRepository) {
        this.depoimentosRepository = depoimentosRepository;
    }

    /**
     * Método responsável por obter a lista de depoimentos.
     *
     * @param pageable - Objeto que contém informações sobre a paginação.
     * @return Page<DepoimentosDTO> - Lista de depoimentos.
     */
    public Page<DepoimentosDTO> obterListaDepoimentos(Pageable pageable) {
        return depoimentosRepository.findAll(pageable)
                .map(DepoimentosDTO::new);
    }

}
