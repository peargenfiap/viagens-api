package br.com.pedroargentati.viagens_api.model;

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
public class Depoimentos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private byte[] foto;
    private String depoimento;
    private String nomePessoa;
    private Date dataCriacao;
    private Date dataAtualizacao;

}
