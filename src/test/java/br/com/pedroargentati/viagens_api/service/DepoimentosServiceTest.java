package br.com.pedroargentati.viagens_api.service;

import br.com.pedroargentati.viagens_api.dto.DepoimentosDTO;
import br.com.pedroargentati.viagens_api.exceptions.RecordNotFoundException;
import br.com.pedroargentati.viagens_api.model.Depoimentos;
import br.com.pedroargentati.viagens_api.repository.DepoimentosRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class DepoimentosServiceTest {

    @Mock
    private DepoimentosRepository depoimentosRepository;

    @Mock
    private DataFileService dataFileService;

    @InjectMocks
    private DepoimentosService depoimentosService;

    @Captor
    private ArgumentCaptor<Depoimentos> depoimentoCaptor;

    @Test
    @DisplayName("Deve retornar página de depoimentos quando houver dados")
    void deveRetornarPaginaDeDepoimentos() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 2);
        Depoimentos depoimento1 = Depoimentos.builder()
                .id(1)
                .depoimento("Depoimento 1")
                .nomePessoa("Pessoa 1")
                .dataCriacao(new Date())
                .dataAtualizacao(new Date())
                .build();

        Depoimentos depoimento2 = Depoimentos.builder()
                .id(2)
                .depoimento("Depoimento 2")
                .nomePessoa("Pessoa 2")
                .dataCriacao(new Date())
                .dataAtualizacao(new Date())
                .build();

        Page<Depoimentos> depoimentosPage = new PageImpl<>(Arrays.asList(depoimento1, depoimento2), pageable, 2);

        BDDMockito.given(depoimentosRepository.findAll(pageable)).willReturn(depoimentosPage);

        // Act
        Page<DepoimentosDTO> result = depoimentosService.obterListaDepoimentos(pageable);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals("Depoimento 1", result.getContent().get(0).depoimento());
        assertEquals("Pessoa 1", result.getContent().get(0).nomePessoa());
        assertEquals("Depoimento 2", result.getContent().get(1).depoimento());
        assertEquals("Pessoa 2", result.getContent().get(1).nomePessoa());
    }

    @Test
    @DisplayName("Deve retornar página vazia quando não houver depoimentos")
    void deveRetornarPaginaVaziaQuandoNaoHouverDepoimentos() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 5);
        Page<Depoimentos> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        // Simular o comportamento do repositório
        BDDMockito.given(depoimentosRepository.findAll(pageable)).willReturn(emptyPage);

        // Act
        Page<DepoimentosDTO> result = depoimentosService.obterListaDepoimentos(pageable);

        // Assert
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getContent().size());
    }

    @Test
    void deveRetornarUmaListaDe3DepoimentosRandomicos() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 2);
        Depoimentos depoimento1 = Depoimentos.builder()
                .id(1)
                .depoimento("Depoimento 1")
                .nomePessoa("Pessoa 1")
                .dataCriacao(new Date())
                .dataAtualizacao(new Date())
                .build();

        Depoimentos depoimento2 = Depoimentos.builder()
                .id(2)
                .depoimento("Depoimento 2")
                .nomePessoa("Pessoa 2")
                .dataCriacao(new Date())
                .dataAtualizacao(new Date())
                .build();

        Depoimentos depoimento3 = Depoimentos.builder()
                .id(3)
                .depoimento("Depoimento 3")
                .nomePessoa("Pessoa 3")
                .dataCriacao(new Date())
                .dataAtualizacao(new Date())
                .build();

        Page<Depoimentos> depoimentosPage = new PageImpl<>(Arrays.asList(depoimento1, depoimento2, depoimento3), pageable, 3);

        BDDMockito.given(depoimentosRepository.findRandomDepoimentos(pageable)).willReturn(depoimentosPage);
        // Act
        Page<DepoimentosDTO> result = depoimentosService.obterListaDepoimentosRandomicos(pageable);

        // Assertions
        assertEquals(3, result.getTotalElements());

    }

    @Test
    void deveRetornarUmDepoimentoQuandoInformadoUmDepoimentoComIdValido() throws RecordNotFoundException {
        // Arrange
        Integer id = 1;
        Depoimentos depoimento = Depoimentos.builder()
                .id(id)
                .depoimento("Depoimento 1")
                .nomePessoa("Pessoa 1")
                .dataCriacao(new Date())
                .build();

        BDDMockito.given(depoimentosRepository.findById(id)).willReturn(Optional.of(depoimento));

        // Act
        DepoimentosDTO dto = this.depoimentosService.obterDepoimentoPorId(id);

        // Assert
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(id, dto.id());
        Assertions.assertEquals(depoimento.getDepoimento(), dto.depoimento());
        Assertions.assertEquals(depoimento.getNomePessoa(), dto.nomePessoa());
        Assertions.assertEquals(depoimento.getDataCriacao(), dto.dataCriacao());

    }

    @Test
    void deveLancarUmaExcecaoQuandoInformadoUmDepoimentoComIdInvalido() {
        // Arrange
        Integer id = 1;

        BDDMockito.given(depoimentosRepository.findById(id)).willReturn(Optional.empty());

        // Act + Assert
        Assertions.assertThrows(RecordNotFoundException.class, () -> this.depoimentosService.obterDepoimentoPorId(id));
    }

    @Test
    @DisplayName("Deve incluir um depoimento com idFile como null quando DataFile não for encontrado e throwEx for false")
    void deveIncluirUmDepoimentoComIdFileNullQuandoNaoEncontrado() throws RecordNotFoundException {
        // Arrange
        DepoimentosDTO dto = new DepoimentosDTO(1, null, "Depoimento 1", "Pessoa 1", new Date(), null);

        BDDMockito.given(dataFileService.obterDataFile(Mockito.eq(null), Mockito.eq(false))).willReturn(null);

        // Act
        depoimentosService.incluirDepoimento(dto);

        // Assert
        BDDMockito.then(depoimentosRepository).should().save(depoimentoCaptor.capture());

        Depoimentos depoimentoSaved = depoimentoCaptor.getValue();
        assertEquals(dto.depoimento(), depoimentoSaved.getDepoimento());
        assertEquals(dto.nomePessoa(), depoimentoSaved.getNomePessoa());
        assertNull(depoimentoSaved.getDataFile());
        assertNull(depoimentoSaved.getDataAtualizacao());
    }

}