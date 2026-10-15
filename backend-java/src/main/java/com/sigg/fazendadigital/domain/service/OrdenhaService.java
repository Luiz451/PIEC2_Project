package com.sigg.fazendadigital.domain.service;

import com.sigg.fazendadigital.domain.entities.Animal;
import com.sigg.fazendadigital.domain.entities.Ordenha;
import com.sigg.fazendadigital.domain.interface_adapters.api.dto.OrdenhaRequest;
import com.sigg.fazendadigital.domain.interface_adapters.api.dto.OrdenhaResponse;
import com.sigg.fazendadigital.infraestructure.persistence.AnimalRepository;
import com.sigg.fazendadigital.infraestructure.persistence.OrdenhaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrdenhaService {

    private final OrdenhaRepository ordenhaRepository;
    private final AnimalRepository animalRepository;

    @Transactional
    public OrdenhaResponse cadastrar(OrdenhaRequest request) {
        validarAnimalDaFazenda(request.animalId(), request.fazendaId());
        validarDuplicidade(request, null);

        Ordenha ordenha = new Ordenha();
        aplicarDados(ordenha, request);
        return paraResponse(salvar(ordenha));
    }

    @Transactional(readOnly = true)
    public Page<OrdenhaResponse> listar(UUID fazendaId, UUID animalId, LocalDate dataInicial,
                                       LocalDate dataFinal, Pageable pageable) {
        if (dataInicial != null && dataFinal != null && dataInicial.isAfter(dataFinal)) {
            throw new IllegalArgumentException("A data inicial deve ser anterior ou igual à data final.");
        }
        return ordenhaRepository.buscarPorFiltros(fazendaId, animalId, dataInicial, dataFinal, pageable)
                .map(this::paraResponse);
    }

    @Transactional(readOnly = true)
    public OrdenhaResponse buscar(UUID fazendaId, UUID id) {
        return paraResponse(buscarEntidade(fazendaId, id));
    }

    @Transactional
    public OrdenhaResponse atualizar(UUID fazendaId, UUID id, OrdenhaRequest request) {
        Ordenha ordenha = buscarEntidade(fazendaId, id);
        validarAnimalDaFazenda(request.animalId(), request.fazendaId());
        validarDuplicidade(request, id);
        aplicarDados(ordenha, request);
        return paraResponse(salvar(ordenha));
    }

    @Transactional
    public void remover(UUID fazendaId, UUID id) {
        ordenhaRepository.delete(buscarEntidade(fazendaId, id));
    }

    private void validarAnimalDaFazenda(UUID animalId, UUID fazendaId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Animal não encontrado."));
        if (!fazendaId.equals(animal.getFazendaId())) {
            throw new IllegalArgumentException("O animal informado não pertence à fazenda.");
        }
    }

    private void validarDuplicidade(OrdenhaRequest request, UUID idAtual) {
        String turno = normalizarTurno(request.turno());
        boolean duplicada = idAtual == null
                ? ordenhaRepository.existsByFazendaIdAndAnimalIdAndDataOrdenhaAndTurno(
                        request.fazendaId(), request.animalId(), request.dataOrdenha(), turno)
                : ordenhaRepository.existsByFazendaIdAndAnimalIdAndDataOrdenhaAndTurnoAndIdNot(
                        request.fazendaId(), request.animalId(), request.dataOrdenha(), turno, idAtual);
        if (duplicada) {
            throw new OrdenhaDuplicadaException();
        }
    }

    private Ordenha buscarEntidade(UUID fazendaId, UUID id) {
        return ordenhaRepository.findByIdAndFazendaId(id, fazendaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordenha não encontrada nesta fazenda."));
    }

    private void aplicarDados(Ordenha ordenha, OrdenhaRequest request) {
        ordenha.setFazendaId(request.fazendaId());
        ordenha.setAnimalId(request.animalId());
        ordenha.setDataOrdenha(request.dataOrdenha());
        ordenha.setHoraOrdenha(request.horaOrdenha());
        ordenha.setTurno(normalizarTurno(request.turno()));
        ordenha.setVolumeLitros(request.volumeLitros());
    }

    private Ordenha salvar(Ordenha ordenha) {
        return ordenhaRepository.saveAndFlush(ordenha);
    }

    private String normalizarTurno(String turno) {
        return turno.trim().toUpperCase(Locale.ROOT);
    }

    private OrdenhaResponse paraResponse(Ordenha ordenha) {
        return new OrdenhaResponse(ordenha.getId(), ordenha.getFazendaId(), ordenha.getAnimalId(),
                ordenha.getDataOrdenha(), ordenha.getHoraOrdenha(), ordenha.getTurno(),
                ordenha.getVolumeLitros(), ordenha.getCriadoEm(), ordenha.getAtualizadoEm());
    }
}
