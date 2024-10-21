package br.com.pedroargentati.viagens_api.service;

import br.com.pedroargentati.viagens_api.dto.DepoimentosDTO;
import br.com.pedroargentati.viagens_api.exceptions.RecordNotFoundException;
import br.com.pedroargentati.viagens_api.model.DataFile;
import br.com.pedroargentati.viagens_api.model.Depoimentos;
import br.com.pedroargentati.viagens_api.repository.DepoimentosRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Logger;

@Service
public class DepoimentosService {

    private static final Logger logger = Logger.getLogger(DepoimentosService.class.getName());

    private final DepoimentosRepository depoimentosRepository;
    private final DataFileService dataFileService;

    public DepoimentosService(DepoimentosRepository depoimentosRepository, DataFileService dataFileService) {
        this.depoimentosRepository = depoimentosRepository;
        this.dataFileService = dataFileService;
    }

    /**
     * Método responsável por obter a lista de depoimentos.
     *
     * @param pageable - Objeto que contém informações sobre a paginação.
     * @return Page<DepoimentosDTO> - Lista de depoimentos.
     */
    public Page<DepoimentosDTO> obterListaDepoimentos(Pageable pageable) {
        logger.info("Obtendo lista de depoimentos");
        return depoimentosRepository.findAll(pageable)
                .map(DepoimentosDTO::new);
    }

    /**
     * Método responsável por obter a lista de depoimentos randômicos.
     *
     * @param pageable - Objeto que contém informações sobre a paginação.
     * @return Page<DepoimentosDTO> - Lista de depoimentos randômicos.
     */
    public Page<DepoimentosDTO> obterListaDepoimentosRandomicos(Pageable pageable) {
        logger.info("Obtendo lista de depoimentos randômicos.");

        Page<Depoimentos> depoimentosPage = depoimentosRepository.findRandomDepoimentos(pageable);

        return depoimentosPage.map(DepoimentosDTO::new);
    }

    /*
     * Método responsável por obter um depoimento por ID.
     *
     * @param id - ID do depoimento.
     * @return Depoimentos - A entidade depoimento.
     * @throws RecordNotFoundException - Se o depoimento não for encontrado.
     */
    public DepoimentosDTO obterDepoimentoPorId(Integer id) throws RecordNotFoundException {
        logger.info(String.format("Obtendo depoimento por ID %d", id));
        return depoimentosRepository.findById(id).map(DepoimentosDTO::new)
                .orElseThrow(() -> new RecordNotFoundException("Depoimento com ID " + id + " não encontrado"));
    }

    /**
     * Método responsável por incluir um depoimento.
     *
     * @param dto - Dados do depoimento.
     */
    @Transactional
    public void incluirDepoimento(DepoimentosDTO dto) throws RecordNotFoundException {
        logger.info("Incluindo depoimento" + dto.toString());
        DataFile dataFile = dto.idFile() != null
                ? this.dataFileService.obterDataFile(dto.idFile())
                : null;

        Depoimentos depoimento = Depoimentos.builder()
                .depoimento(dto.depoimento())
                .nomePessoa(dto.nomePessoa())
                .dataFile(dataFile)
                .dataCriacao(new Date())
                .build();

        depoimentosRepository.save(depoimento);
        logger.info("Depoimento incluído com sucesso");
    }

    /*
     * Método responsável por atualizar um depoimento.
     *
     * @param dto - Dados do depoimento.
     */
    @Transactional
    public void atualizarDepoimento(DepoimentosDTO dto) throws RecordNotFoundException {
        logger.info("Atualizando depoimento" + dto.toString());
        Depoimentos depoimento = depoimentosRepository.findById(dto.id())
                .orElseThrow(() -> new RecordNotFoundException("Depoimento com ID " + dto.id() + " não encontrado"));

        DataFile dataFile = dto.idFile() != null
                ? this.dataFileService.obterDataFile(dto.idFile())
                : null;

        depoimento = Depoimentos.builder()
                .id(depoimento.getId())
                .depoimento(dto.depoimento())
                .nomePessoa(dto.nomePessoa())
                .dataFile(dataFile)
                .dataCriacao(depoimento.getDataCriacao())
                .dataAtualizacao(new Date())
                .build();

        depoimentosRepository.save(depoimento);
        logger.info("Depoimento atualizado com sucesso");
    }

    /*
     * Método responsável por excluir um depoimento.
     *
     * @param id - ID do depoimento.
     */
    @Transactional
    public Depoimentos excluirDepoimento(Integer id) throws RecordNotFoundException {
        logger.info(String.format("Excluindo depoimento por ID %d", id));
        Depoimentos depoimento = depoimentosRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Depoimento com ID " + id + " não encontrado"));

        depoimentosRepository.delete(depoimento);

        logger.info("Depoimento excluído com sucesso");
        return depoimento;
    }

}
