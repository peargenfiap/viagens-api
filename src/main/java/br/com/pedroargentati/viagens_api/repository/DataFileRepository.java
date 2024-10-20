package br.com.pedroargentati.viagens_api.repository;

import br.com.pedroargentati.viagens_api.model.DataFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataFileRepository extends JpaRepository<DataFile, String>  {
}
