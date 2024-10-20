package br.com.pedroargentati.viagens_api.service;

import br.com.pedroargentati.viagens_api.dto.DepoimentosDTO;
import br.com.pedroargentati.viagens_api.model.Depoimentos;
import br.com.pedroargentati.viagens_api.repository.DepoimentosRepository;
import jakarta.transaction.Transactional;
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

    /**
     * Método responsável por incluir um depoimento.
     *
     * @param dto - Dados do depoimento.
     */
    @Transactional
    public void incluirDepoimento(DepoimentosDTO dto) {
        Depoimentos depoimento = new Depoimentos(dto);
        depoimentosRepository.save(depoimento);
    }

}
