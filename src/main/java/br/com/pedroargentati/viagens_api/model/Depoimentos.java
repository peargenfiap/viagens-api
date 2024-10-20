package br.com.pedroargentati.viagens_api.model;

import br.com.pedroargentati.viagens_api.dto.DepoimentosDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Table(name = "depoimentos")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Depoimentos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "idFile", referencedColumnName = "idFile")
    private DataFile dataFile;

    private String depoimento;
    private String nomePessoa;
    private Date dataCriacao;
    private Date dataAtualizacao;

    public Depoimentos(DepoimentosDTO dto) {
        this.id = dto.id();
        this.depoimento = dto.depoimento();
        this.nomePessoa = dto.nomePessoa();
        this.dataCriacao = dto.dataCriacao();
        this.dataAtualizacao = dto.dataAtualizacao();
    }

}
