package br.com.pedroargentati.viagens_api.dto;

import br.com.pedroargentati.viagens_api.model.DataFile;

import java.util.Base64;

public record DataFileDTO(String idFile, String fileName, String mediaType, Integer size, byte[] data) {

    public static DataFileDTO toDataFile(DataFile dataFile) {
        return new DataFileDTO(
                dataFile.getIdFile(),
                dataFile.getFileName(),
                dataFile.getMediaType(),
                dataFile.getSize(),
                dataFile.getData() != null ? Base64.getEncoder().encodeToString(dataFile.getData()).getBytes() : null
        );
    }

    public byte[] getDecodedData() {
        return Base64.getDecoder().decode(this.data);
    }

}
