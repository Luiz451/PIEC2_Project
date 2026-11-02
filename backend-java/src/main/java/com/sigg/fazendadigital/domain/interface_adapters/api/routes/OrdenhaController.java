package com.sigg.fazendadigital.domain.interface_adapters.api.routes;

import com.sigg.fazendadigital.domain.entities.Ordenha;
import com.sigg.fazendadigital.domain.service.OrdenhaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ordenhas")
@RequiredArgsConstructor
public class OrdenhaController {

    private final OrdenhaService ordenhaService;

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Ordenha ordenha) {
        try {
            Ordenha novaOrdenha = ordenhaService.registrarOrdenha(ordenha);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaOrdenha);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Ordenha>> listar() {
        return ResponseEntity.ok(ordenhaService.listarTodas());
    }

    @GetMapping("/animal/{animalId}")
    public ResponseEntity<List<Ordenha>> listarPorAnimal(@PathVariable UUID animalId) {
        return ResponseEntity.ok(ordenhaService.listarPorAnimal(animalId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(ordenhaService.buscarPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable UUID id) {
        try {
            ordenhaService.remover(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}