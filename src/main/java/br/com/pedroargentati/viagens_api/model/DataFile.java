package br.com.pedroargentati.viagens_api.model;

import br.com.pedroargentati.viagens_api.dto.DataFileDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Table(name = "DataFile")
@Entity()
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "idFile")
public class DataFile {

    @Id
    @Column(name = "IdFile")
    private String idFile;

    @Column(name = "FileName")
    private String fileName;

    @Column(name = "MediaType")
    private String mediaType;

    @Column(name = "Size")
    private Integer size;

    @Column(name = "data")
    private byte[] data;

    @Column(name = "createdAt")
    private Date createdAt;

    public DataFile(DataFileDTO dto) {
        this.idFile = dto.idFile();
        this.fileName = dto.fileName();
        this.mediaType = dto.mediaType();
        this.size = dto.size();
        this.data = dto.data();
        this.createdAt = new Date();
    }

}
