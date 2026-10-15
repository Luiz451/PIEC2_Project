package com.sigg.fazendadigital.domain.service;

import com.sigg.fazendadigital.domain.entities.Animal;
import com.sigg.fazendadigital.domain.entities.Ordenha;
import com.sigg.fazendadigital.domain.interface_adapters.api.dto.OrdenhaRequest;
import com.sigg.fazendadigital.domain.interface_adapters.api.dto.OrdenhaResponse;
import com.sigg.fazendadigital.infraestructure.persistence.AnimalRepository;
import com.sigg.fazendadigital.infraestructure.persistence.OrdenhaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdenhaServiceTest {

    @Mock
    private OrdenhaRepository ordenhaRepository;

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private OrdenhaService ordenhaService;

    @Test
    void cadastraOrdenhaEPadronizaTurno() {
        UUID fazendaId = UUID.randomUUID();
        UUID animalId = UUID.randomUUID();
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal(animalId, fazendaId)));
        when(ordenhaRepository.existsByFazendaIdAndAnimalIdAndDataOrdenhaAndTurno(
                fazendaId, animalId, LocalDate.of(2026, 10, 15), "MANHÃ")).thenReturn(false);
        when(ordenhaRepository.saveAndFlush(any(Ordenha.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrdenhaResponse response = ordenhaService.cadastrar(request(fazendaId, animalId, " manhã "));

        assertEquals("MANHÃ", response.turno());
        assertEquals(new BigDecimal("12.500"), response.volumeLitros());
        verify(ordenhaRepository).saveAndFlush(any(Ordenha.class));
    }

    @Test
    void rejeitaSegundaOrdenhaDoAnimalNoMesmoTurno() {
        UUID fazendaId = UUID.randomUUID();
        UUID animalId = UUID.randomUUID();
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal(animalId, fazendaId)));
        when(ordenhaRepository.existsByFazendaIdAndAnimalIdAndDataOrdenhaAndTurno(
                fazendaId, animalId, LocalDate.of(2026, 10, 15), "TARDE")).thenReturn(true);

        assertThrows(OrdenhaDuplicadaException.class,
                () -> ordenhaService.cadastrar(request(fazendaId, animalId, "tarde")));

        verify(ordenhaRepository, never()).saveAndFlush(any(Ordenha.class));
    }

    @Test
    void rejeitaAnimalDeOutraFazenda() {
        UUID fazendaId = UUID.randomUUID();
        UUID outraFazendaId = UUID.randomUUID();
        UUID animalId = UUID.randomUUID();
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal(animalId, outraFazendaId)));

        assertThrows(IllegalArgumentException.class,
                () -> ordenhaService.cadastrar(request(fazendaId, animalId, "manhã")));

        verify(ordenhaRepository, never()).saveAndFlush(any(Ordenha.class));
    }

    private Animal animal(UUID animalId, UUID fazendaId) {
        Animal animal = new Animal();
        animal.setId(animalId);
        animal.setFazendaId(fazendaId);
        return animal;
    }

    private OrdenhaRequest request(UUID fazendaId, UUID animalId, String turno) {
        return new OrdenhaRequest(fazendaId, animalId, LocalDate.of(2026, 10, 15),
                LocalTime.of(6, 30), turno, new BigDecimal("12.500"));
    }
}
