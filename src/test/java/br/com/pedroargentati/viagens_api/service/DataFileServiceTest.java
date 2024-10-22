package br.com.pedroargentati.viagens_api.service;

import br.com.pedroargentati.viagens_api.dto.DataFileDTO;
import br.com.pedroargentati.viagens_api.exceptions.BusinessException;
import br.com.pedroargentati.viagens_api.exceptions.FileProcessingException;
import br.com.pedroargentati.viagens_api.exceptions.RecordNotFoundException;
import br.com.pedroargentati.viagens_api.model.DataFile;
import br.com.pedroargentati.viagens_api.repository.DataFileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DataFileServiceTest {

    @InjectMocks
    private DataFileService dataFileService;

    @Mock
    private DataFileRepository dataFileRepository;

    @Mock
    private DataFileDTO dataFileDTO;

    @Test
    @DisplayName("[obterDataFilePorChave] - Deve retornar um DataFileDTO quando chave válida")
    void deveRetornarUmDataFileDTOQuandoChaveValida() {
        // Arrange
        String idFile = "ec15d2f24e19845b77489e0a2af2f8af4bf36560";

        DataFile mockDataFile = new DataFile();
        mockDataFile.setIdFile(idFile);

        BDDMockito.given(dataFileRepository.findById(idFile)).willReturn(Optional.of(mockDataFile));

        // Act
        DataFileDTO result = dataFileService.obterDataFilePorChave(idFile);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(idFile, result.idFile());
    }

    @Test
    @DisplayName("[obterDataFilePorChave] - Deve retornar null quando chave inválida")
    void deveRetornarNullQuandoChaveInvalida() {
        // Arrange
        String idFile = "chave-invalida";

        BDDMockito.given(dataFileRepository.findById(idFile)).willReturn(Optional.empty());

        // Act
        DataFileDTO result = dataFileService.obterDataFilePorChave(idFile);

        // Assert
        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("[obterDataFile] - Deve retornar um DataFile quando chave válida e não deve lançar uma exceção")
    void deveRetornarUmDataFileQuandoChaveValidaENaoDeveLancarUmaExcecao() throws RecordNotFoundException {
        // Arrange
        String idFile = "ec15d2f24e19845b77489e0a2af2f8af4bf36560";

        DataFile mockDataFile = new DataFile();
        mockDataFile.setIdFile(idFile);

        BDDMockito.given(dataFileRepository.findById(idFile)).willReturn(Optional.of(mockDataFile));
        // Act
        DataFile result = dataFileService.obterDataFile(idFile);

        // Assert
        Assertions.assertEquals(idFile, result.getIdFile());
        Assertions.assertDoesNotThrow(() -> new RecordNotFoundException());
    }

    @Test
    @DisplayName("[obterDataFile] - Deve lançar uma exceção quando uma chave inválida for informada")
    void deveLancarExcecaoQuandoChaveForInvalida() {
        // Arrange
        String idFile = "chave-invalida";

        BDDMockito.given(dataFileRepository.findById(idFile)).willReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(RecordNotFoundException.class, () -> dataFileService.obterDataFile(idFile));
    }

    @Test
    void deveLancarExcecaoAoIncluirUmDataFileSemDataFile() {
        // Arrange
        DataFileDTO dto = null;

        // Act + Assert
        Assertions.assertThrows(BusinessException.class, () -> this.dataFileService.incluirDataFile(dto));
    }

    @Test
    void deveLancarExcecaoAoIncluirUmDataFileComDataNull() {
        // Arrange
        DataFileDTO dto = new DataFileDTO(
                "Id",
                "Arquivo",
                "pdf",
                128,
                null);

        // Act + Assert
        Assertions.assertThrows(FileProcessingException.class, () -> this.dataFileService.incluirDataFile(dto));
    }

    @Test
    void deveLancarExcecaoAoIncluirUmDataFileComDataVazio() {
        // Arrange
        DataFileDTO dto = new DataFileDTO(
                "Id",
                "Arquivo",
                "pdf",
                128,
                new byte[0]);

        // Act + Assert
        Assertions.assertThrows(FileProcessingException.class, () -> this.dataFileService.incluirDataFile(dto));
    }

}