package com.sigg.fazendadigital.domain.service;

import com.sigg.fazendadigital.domain.entities.Ordenha;
import com.sigg.fazendadigital.infraestructure.persistence.OrdenhaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdenhaService {

    private final OrdenhaRepository ordenhaRepository;

    @Transactional
    public Ordenha registrarOrdenha(Ordenha ordenha) {
        if (!"manha".equalsIgnoreCase(ordenha.getTurno()) && !"tarde".equalsIgnoreCase(ordenha.getTurno())) {
            throw new IllegalArgumentException("O turno deve ser estritamente 'manha' ou 'tarde'.");
        }

        boolean duplicata = ordenhaRepository.existsByAnimalIdAndDataAndTurno(
                ordenha.getAnimal().getId(),
                ordenha.getData(),
                ordenha.getTurno()
        );

        if (duplicata) {
            throw new IllegalStateException(
                    String.format("Já existe uma ordenha registada para o animal no turno da %s na data %s.",
                            ordenha.getTurno(), ordenha.getData())
            );
        }

        verificarAlertaDeVariacao(ordenha);

        return ordenhaRepository.save(ordenha);
    }

    public List<Ordenha> listarTodas() {
        return ordenhaRepository.findAll();
    }

    public List<Ordenha> listarPorAnimal(UUID animalId) {
        return ordenhaRepository.findByAnimalId(animalId);
    }

    public Ordenha buscarPorId(UUID id) {
        return ordenhaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registo de ordenha não encontrado."));
    }

    @Transactional
    public void remover(UUID id) {
        if (!ordenhaRepository.existsById(id)) {
            throw new IllegalArgumentException("Registo de ordenha inexistente.");
        }
        ordenhaRepository.deleteById(id);
    }

    private void verificarAlertaDeVariacao(Ordenha novaOrdenha) {
        try {
            List<Ordenha> historico = ordenhaRepository.findByAnimalId(novaOrdenha.getAnimal().getId());

            LocalDate dataLimite = novaOrdenha.getData().minusDays(7);

            double somaLitros = historico.stream()
                    .filter(o -> o.getData().isAfter(dataLimite) && o.getData().isBefore(novaOrdenha.getData()))
                    .mapToDouble(o -> o.getLitros().doubleValue())
                    .sum();

            long qtdRegistos = historico.stream()
                    .filter(o -> o.getData().isAfter(dataLimite) && o.getData().isBefore(novaOrdenha.getData()))
                    .count();

            if (qtdRegistos > 0) {
                double media = somaLitros / qtdRegistos;
                double valorAtual = novaOrdenha.getLitros().doubleValue();

                double variacao = Math.abs((valorAtual - media) / media) * 100;

                if (variacao > 30.0) {
                    log.warn("ALERTA DE PRODUÇÃO: O animal {} apresentou uma variação de {}.2f%% em relação à média dos últimos 7 dias.",
                            novaOrdenha.getAnimal().getId(), variacao);
                }
            }
        } catch (Exception e) {
            log.error("Falha ao calcular a variação de produção para o alerta", e);
        }
    }
}