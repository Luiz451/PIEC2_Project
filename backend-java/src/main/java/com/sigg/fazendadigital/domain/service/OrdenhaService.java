package com.sigg.fazendadigital.domain.service;

import com.sigg.fazendadigital.domain.entities.Animal;
import com.sigg.fazendadigital.domain.entities.Ordenha;
import com.sigg.fazendadigital.domain.interface_adapters.api.dto.OrdenhaRequest;
import com.sigg.fazendadigital.domain.interface_adapters.api.dto.OrdenhaResponse;
import com.sigg.fazendadigital.infraestructure.persistence.AnimalRepository;
import com.sigg.fazendadigital.infraestructure.persistence.OrdenhaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.text.Normalizer;
import java.util.Locale;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
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
        verificarAlertaDeVariacao(ordenha);
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
        ordenha.setTemperatura(request.temperatura());
        ordenha.setOperadorId(request.operadorId());
        ordenha.setObservacao(request.observacao());
    }

    private Ordenha salvar(Ordenha ordenha) {
        return ordenhaRepository.saveAndFlush(ordenha);
    }

    private String normalizarTurno(String turno) {
        String normalizado = Normalizer.normalize(turno.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "").toUpperCase(Locale.ROOT);
        if (!normalizado.equals("MANHA") && !normalizado.equals("TARDE")) {
            throw new IllegalArgumentException("O turno deve ser manhã ou tarde.");
        }
        return normalizado;
    }

    private void verificarAlertaDeVariacao(Ordenha ordenha) {
        List<Ordenha> historico = ordenhaRepository.findByAnimalIdAndDataOrdenhaBetween(
                ordenha.getAnimalId(), ordenha.getDataOrdenha().minusDays(7),
                ordenha.getDataOrdenha().minusDays(1));
        if (historico.isEmpty()) {
            return;
        }
        double media = historico.stream().mapToDouble(o -> o.getVolumeLitros().doubleValue())
                .average().orElse(0);
        if (media > 0) {
            double variacao = Math.abs((ordenha.getVolumeLitros().doubleValue() - media) / media) * 100;
            if (variacao > 30) {
                log.warn("ALERTA DE PRODUÇÃO: animal {} variou {}% em relação à média dos últimos 7 dias.",
                        ordenha.getAnimalId(), String.format(Locale.ROOT, "%.2f", variacao));
            }
        }
    }

    private OrdenhaResponse paraResponse(Ordenha ordenha) {
        return new OrdenhaResponse(ordenha.getId(), ordenha.getFazendaId(), ordenha.getAnimalId(),
                ordenha.getDataOrdenha(), ordenha.getHoraOrdenha(), ordenha.getTurno(),
                ordenha.getVolumeLitros(), ordenha.getTemperatura(), ordenha.getOperadorId(),
                ordenha.getObservacao(), ordenha.getCriadoEm(), ordenha.getAtualizadoEm());
    }
}
