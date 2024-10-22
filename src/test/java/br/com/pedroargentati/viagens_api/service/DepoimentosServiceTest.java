package br.com.pedroargentati.viagens_api.service;

import br.com.pedroargentati.viagens_api.dto.DepoimentosDTO;
import br.com.pedroargentati.viagens_api.model.Depoimentos;
import br.com.pedroargentati.viagens_api.repository.DepoimentosRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DepoimentosServiceTest {

    @Mock
    private DepoimentosRepository depoimentosRepository;

    @InjectMocks
    private DepoimentosService depoimentosService;

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

}