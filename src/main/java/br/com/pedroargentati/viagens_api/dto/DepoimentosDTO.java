package br.com.pedroargentati.viagens_api.dto;

import br.com.pedroargentati.viagens_api.model.DataFile;
import br.com.pedroargentati.viagens_api.model.Depoimentos;

import java.util.Date;

public record DepoimentosDTO(Integer id,
                             DataFile dataFile,
                             String depoimento,
                             String nomePessoa,
                             Date dataCriacao,
                             Date dataAtualizacao) {

    public DepoimentosDTO(Depoimentos depoimentos) {
        this(depoimentos.getId(),
                depoimentos.getDataFile(),
                depoimentos.getDepoimento(),
                depoimentos.getNomePessoa(),
                depoimentos.getDataCriacao(),
                depoimentos.getDataAtualizacao());
    }

    @Override
    public String toString() {
        return "DepoimentosDTO{" +
                "id=" + id +
                ", dataFile=" + dataFile.toString() +
                ", depoimento='" + depoimento + '\'' +
                ", nomePessoa='" + nomePessoa + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                '}';
    }
}
