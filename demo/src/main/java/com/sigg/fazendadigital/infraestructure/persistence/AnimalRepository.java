package com.sigg.fazendadigital.infraestructure.persistence;

import com.sigg.fazendadigital.domain.entities.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, UUID> {
    Optional<Animal> findByFazendaIdAndBrinco(UUID fazendaId, String brinco);
}
