package com.sigg.fazendadigital.domain.service;

import com.sigg.fazendadigital.domain.entities.Animal;
import com.sigg.fazendadigital.infraestructure.persistence.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;

    public Animal cadastrarAnimal(Animal animal) {
        return animalRepository.save(animal);
    }

    public List<Animal> listarAnimais() {
        return animalRepository.findAll();
    }

    public Animal buscarPorID(UUID id) {
        return animalRepository.findById(id).orElseThrow(() -> new RuntimeException("Animal não encontrado"));
    }

    public Animal atualizarAnimal(UUID id, Animal animalAtualizado) {
        Animal animal = buscarPorID(id);
        animal.setNome(animalAtualizado.getNome());
        animal.setBrinco(animalAtualizado.getBrinco());
        animal.setDataNascimento(animalAtualizado.getDataNascimento());
        animal.setFotoUrl(animalAtualizado.getFotoUrl());
        animal.setSexo(animalAtualizado.getSexo());
        animal.setStatus(animalAtualizado.getStatus());
        return animalRepository.save(animal);
    }

    public void removerAnimal(UUID id) {
        animalRepository.deleteById(id);
    }
}
