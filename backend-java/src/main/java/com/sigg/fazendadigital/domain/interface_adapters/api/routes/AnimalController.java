package com.sigg.fazendadigital.domain.interface_adapters.api.routes;

import com.sigg.fazendadigital.domain.entities.Animal;
import com.sigg.fazendadigital.domain.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/animals")
@RequiredArgsConstructor
public class AnimalController {

    private AnimalService animalService;

    @PostMapping
    public ResponseEntity<Animal> cadastrar(@RequestBody Animal animal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(animalService.cadastrarAnimal(animal));
    }

    @GetMapping
    public ResponseEntity<List<Animal>> listarAnimais() {
        return ResponseEntity.status(HttpStatus.OK).body(animalService.listarAnimais());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(animalService.buscarPorID(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animal> atualizar(@PathVariable UUID id, @RequestBody Animal animal) {
        return ResponseEntity.ok(animalService.atualizarAnimal(id, animal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        animalService.removerAnimal(id);
        return ResponseEntity.noContent().build();
    }
}
