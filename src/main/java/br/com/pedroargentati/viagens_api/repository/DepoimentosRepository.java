package br.com.pedroargentati.viagens_api.repository;

import br.com.pedroargentati.viagens_api.model.Depoimentos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepoimentosRepository extends JpaRepository<Depoimentos, Integer> {

    @Query(value = "SELECT * FROM depoimentos ORDER BY RAND()", nativeQuery = true)
    Page<Depoimentos> findRandomDepoimentos(Pageable pageable);

}
