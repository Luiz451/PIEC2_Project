package com.sigg.fazendadigital.infraestructure.persistence;

import com.sigg.fazendadigital.domain.entities.Ordenha;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface OrdenhaRepository extends JpaRepository<Ordenha, UUID> {

    boolean existsByFazendaIdAndAnimalIdAndDataOrdenhaAndTurno(
            UUID fazendaId, UUID animalId, LocalDate dataOrdenha, String turno);

    boolean existsByFazendaIdAndAnimalIdAndDataOrdenhaAndTurnoAndIdNot(
            UUID fazendaId, UUID animalId, LocalDate dataOrdenha, String turno, UUID id);

    Optional<Ordenha> findByIdAndFazendaId(UUID id, UUID fazendaId);

    @Query("""
            SELECT o FROM Ordenha o
            WHERE o.fazendaId = :fazendaId
              AND (:animalId IS NULL OR o.animalId = :animalId)
              AND (:dataInicial IS NULL OR o.dataOrdenha >= :dataInicial)
              AND (:dataFinal IS NULL OR o.dataOrdenha <= :dataFinal)
            """)
    Page<Ordenha> buscarPorFiltros(
            @Param("fazendaId") UUID fazendaId,
            @Param("animalId") UUID animalId,
            @Param("dataInicial") LocalDate dataInicial,
            @Param("dataFinal") LocalDate dataFinal,
            Pageable pageable);
}
