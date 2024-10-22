package br.com.pedroargentati.viagens_api.service;

import br.com.pedroargentati.viagens_api.dto.DataFileDTO;
import br.com.pedroargentati.viagens_api.exceptions.BusinessException;
import br.com.pedroargentati.viagens_api.exceptions.FileProcessingException;
import br.com.pedroargentati.viagens_api.exceptions.HashGenerationException;
import br.com.pedroargentati.viagens_api.exceptions.RecordNotFoundException;
import br.com.pedroargentati.viagens_api.model.DataFile;
import br.com.pedroargentati.viagens_api.repository.DataFileRepository;
import br.com.pedroargentati.viagens_api.util.crypto.HashUtil;
import br.com.pedroargentati.viagens_api.util.io.FileSecurityCheck;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.Optional;

@Service
public class DataFileService {

    private final DataFileRepository dataFileRepository;

    private final FileSecurityCheck fileSecurityCheck;

    @Autowired
    public DataFileService(DataFileRepository dataFileRepository, FileSecurityCheck fileSecurityCheck) {
        this.dataFileRepository = dataFileRepository;
        this.fileSecurityCheck = fileSecurityCheck;
    }

    public DataFileDTO obterDataFilePorChave(String idFile) {
        Optional<DataFile> dataFile = dataFileRepository.findById(idFile);
        return dataFile.map(DataFileDTO::toDataFile)
                .orElse(null);
    }

    public DataFile obterDataFile(String idFile, boolean throwEx) throws RecordNotFoundException {
        Optional<DataFile> dataFile = dataFileRepository.findById(idFile);
        if (throwEx) {
            return dataFile.orElseThrow(() -> new RecordNotFoundException("Arquivo com ID " + idFile + " não encontrado"));
        }
        return dataFile.orElse(null);
    }

    public DataFile obterDataFile(String idFile) throws RecordNotFoundException {
        return this.obterDataFile(idFile, true);
    }

    /*
     * Inclui um arquivo de dados.
     *
     * @param dto - Dados do arquivo.
     * @throws HashGenerationException - Erro ao gerar o hash do arquivo.
     * @throws FileProcessingException - Erro ao processar o arquivo.
     */
    @Transactional
    public void incluirDataFile(DataFileDTO dto) throws HashGenerationException, FileProcessingException, BusinessException {
        if (dto == null) {
            throw new BusinessException("É necessário informar um corpo na requisição.");
        }

        DataFile dataFile = new DataFile(dto);

        // Verificação de dados
        if (dataFile.getData() == null || dataFile.getData().length == 0) {
            throw new FileProcessingException("Dados do arquivo estão vazios ou inválidos.");
        }

        try (ByteArrayInputStream bis = new ByteArrayInputStream(dataFile.getData())) {
            this.fileSecurityCheck.check(dataFile.getFileName(), dataFile.getMediaType(), bis);
        } catch (Exception e) {
            throw new FileProcessingException("Erro ao processar o arquivo", e);
        }

        String hash = HashUtil.calcularHash(dataFile.getData(), "SHA-1");
        dataFile.setIdFile(hash);

        Optional<DataFile> dataFileExistente = dataFileRepository.findById(dataFile.getIdFile());
        if (dataFileExistente.isPresent()) {
            DataFile file = dataFileExistente.get();
            dataFile.setFileName(file.getFileName());
            dataFile.setMediaType(file.getMediaType());
            dataFile.setSize(file.getSize());
            dataFile.setCreatedAt(file.getCreatedAt());
        } else {
            dataFile.setCreatedAt(new Date());
            dataFileRepository.save(dataFile);
        }

    }

}
