package br.com.pedroargentati.viagens_api.service;

import br.com.pedroargentati.viagens_api.dto.DataFileDTO;
import br.com.pedroargentati.viagens_api.exceptions.BusinessException;
import br.com.pedroargentati.viagens_api.exceptions.FileProcessingException;
import br.com.pedroargentati.viagens_api.exceptions.RecordNotFoundException;
import br.com.pedroargentati.viagens_api.exceptions.ViagensException;
import br.com.pedroargentati.viagens_api.model.DataFile;
import br.com.pedroargentati.viagens_api.repository.DataFileRepository;
import br.com.pedroargentati.viagens_api.util.crypto.HashUtil;
import br.com.pedroargentati.viagens_api.util.io.FileSecurityCheck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DataFileServiceTest {

    @InjectMocks
    private DataFileService dataFileService;

    @Mock
    private DataFileRepository dataFileRepository;

    @Mock
    private DataFileDTO dataFileDTO;

    @Captor
    private ArgumentCaptor<DataFile> dataFileCaptor;

    private DataFileDTO dto;

    @Mock
    private FileSecurityCheck fileSecurityCheck;

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
    @DisplayName("[incluirDataFile] - Deve lançar uma exceção ao incluir um DataFile sem DataFile")
    void deveLancarExcecaoAoIncluirUmDataFileSemDataFile() {
        // Arrange
        DataFileDTO dto = null;

        // Act + Assert
        Assertions.assertThrows(BusinessException.class, () -> this.dataFileService.incluirDataFile(dto));
    }

    @Test
    @DisplayName("[incluirDataFile] - Deve lançar uma exceção ao incluir um DataFile com DataFile null")
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
    @DisplayName("[incluirDataFile] - Deve lançar uma exceção ao incluir um DataFile com DataFile vazio")
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

    @Test
    @DisplayName("[incluirDataFile] - Deve lançar uma exceção ao incluir um DataFile com DataFile inválido")
    void deveLancarFileProcessingExceptionQuandoCheckFalhar() throws Exception {
        // Arrange
        String idFile = "ec15d2f24e19845b77489e0a2af2f8af4bf36560";
        String fileName = "arquivo.pdf";
        String mediaType = "application/pdf";
        byte[] data = "conteúdo do arquivo".getBytes();
        Integer size = 120;
        DataFileDTO dataFileDTO = new DataFileDTO(idFile, fileName, mediaType, size, data);

        // Simular exceção quando o método check for chamado
        Mockito.doThrow(new FileProcessingException("Erro ao processar o arquivo"))
                .when(fileSecurityCheck)
                .check(Mockito.eq(fileName), Mockito.eq(mediaType), Mockito.any(ByteArrayInputStream.class));

        // Act & Assert
        Assertions.assertThrows(FileProcessingException.class, () -> dataFileService.incluirDataFile(dataFileDTO));
    }

    @Test
    @DisplayName("[incluirDataFile] - Deve chamar o FileSecurityCheck quando os parâmetros estiverem corretos")
    void deveChamarOFileSecurityCheckQuandoOsPrametrosEstiveremCorretos() throws ViagensException, IOException {
        // Arrange
        String idFile = "ec15d2f24e19845b77489e0a2af2f8af4bf36560";
        String fileName = "arquivo.pdf";
        String mediaType = "application/pdf";
        byte[] data = "conteúdo do arquivo".getBytes();
        Integer size = 120;

        // Act
        dataFileService.incluirDataFile(new DataFileDTO(idFile, fileName, mediaType, size, data));

        // Assert
        Mockito.verify(fileSecurityCheck).check(Mockito.eq(fileName), Mockito.eq(mediaType), Mockito.any(ByteArrayInputStream.class));
    }

    @Test
    @DisplayName("[incluirDataFile] - Deve salvar DataFile quando não existir DataFile existente")
    void deveSalvarDataFileQuandoNaoExistirDataFileExistente() throws ViagensException {
        // Arrange
        String idFile = "ec15d2f24e19845b77489e0a2af2f8af4bf36560";
        String fileName = "arquivo.pdf";
        String mediaType = "application/pdf";
        byte[] data = "conteúdo do arquivo".getBytes();
        Integer size = 120;

        this.dto = new DataFileDTO(idFile, fileName, mediaType, size, data);

        // Act
        dataFileService.incluirDataFile(this.dto);

        // Assert
        BDDMockito.then(dataFileRepository).should().save(dataFileCaptor.capture());
        String hash = HashUtil.calcularHash(this.dto.data(), "SHA-1");

        DataFile dataFileSaved = dataFileCaptor.getValue();
        Assertions.assertEquals(hash, dataFileSaved.getIdFile());
        Assertions.assertEquals(fileName, dataFileSaved.getFileName());
        Assertions.assertEquals(mediaType, dataFileSaved.getMediaType());
        Assertions.assertEquals(size, dataFileSaved.getSize());
        Assertions.assertEquals(data, dataFileSaved.getData());
        Assertions.assertNotNull(dataFileSaved.getCreatedAt());
    }

    @Test
    @DisplayName("[incluirDataFile] - Deve reutilizar DataFile existente")
    void deveReutilizarDataFileExistente() throws ViagensException {
        // Arrange
        String fileName = "arquivo.pdf";
        String mediaType = "application/pdf";
        byte[] data = "conteúdo do arquivo".getBytes();
        Integer size = 120;
        String idFile = HashUtil.calcularHash(data, "SHA-1");

        this.dto = new DataFileDTO(idFile, fileName, mediaType, size, data);

        DataFile existingDataFile = new DataFile();
        existingDataFile.setIdFile(idFile);
        existingDataFile.setFileName(fileName);
        existingDataFile.setMediaType(mediaType);
        existingDataFile.setSize(size);
        existingDataFile.setData(data);
        existingDataFile.setCreatedAt(new Date());

        BDDMockito.given(dataFileRepository.findById(idFile))
                .willReturn(Optional.of(existingDataFile));

        // Act
        dataFileService.incluirDataFile(this.dto);

        // Assert
        BDDMockito.then(dataFileRepository).should(BDDMockito.never()).save(BDDMockito.any(DataFile.class));

        Assertions.assertEquals(fileName, existingDataFile.getFileName());
        Assertions.assertEquals(mediaType, existingDataFile.getMediaType());
        Assertions.assertEquals(size, existingDataFile.getSize());
        Assertions.assertEquals(data, existingDataFile.getData());
        Assertions.assertNotNull(existingDataFile.getCreatedAt());
    }

}