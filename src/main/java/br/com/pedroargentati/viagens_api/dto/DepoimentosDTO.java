package br.com.pedroargentati.viagens_api.dto;

import br.com.pedroargentati.viagens_api.model.Depoimentos;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public record DepoimentosDTO(Integer id,
                             byte[] foto,
                             String depoimento,
                             String nomePessoa,
                             Date dataCriacao,
                             Date dataAtualizacao) {

    public DepoimentosDTO(Depoimentos depoimentos) {
        this(depoimentos.getId(),
                depoimentos.getFoto(),
                depoimentos.getDepoimento(),
                depoimentos.getNomePessoa(),
                depoimentos.getDataCriacao(),
                depoimentos.getDataAtualizacao());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepoimentosDTO that = (DepoimentosDTO) o;
        return id.equals(that.id) &&
                Arrays.equals(foto, that.foto) &&
                depoimento.equals(that.depoimento) &&
                nomePessoa.equals(that.nomePessoa) &&
                dataCriacao.equals(that.dataCriacao) &&
                dataAtualizacao.equals(that.dataAtualizacao);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, depoimento, nomePessoa, dataCriacao, dataAtualizacao);
        result = 31 * result + Arrays.hashCode(foto);
        return result;
    }

    @Override
    public String toString() {
        return "DepoimentosDTO{" +
                "id=" + id +
                ", foto=" + Arrays.toString(foto) +
                ", depoimento='" + depoimento + '\'' +
                ", nomePessoa='" + nomePessoa + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                '}';
    }
}
