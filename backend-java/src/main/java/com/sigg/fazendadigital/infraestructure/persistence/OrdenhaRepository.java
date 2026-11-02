package com.sigg.fazendadigital.infraestructure.persistence;

import com.sigg.fazendadigital.domain.entities.Ordenha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrdenhaRepository extends JpaRepository<Ordenha, UUID> {
    boolean existsByAnimalIdAndDataAndTurno(UUID animalId, LocalDate dataOrdenha, String turno);

    List<Ordenha> findByAnimalId(UUID animalId);

    List<Ordenha> findByDataOrdenha(LocalDate dataOrdenha);

    List<Ordenha> findByTurno(String turno);
}
