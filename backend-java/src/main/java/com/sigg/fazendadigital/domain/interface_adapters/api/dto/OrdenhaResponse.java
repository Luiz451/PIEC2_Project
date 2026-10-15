package com.sigg.fazendadigital.domain.interface_adapters.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

public record OrdenhaResponse(
        UUID id,
        UUID fazendaId,
        UUID animalId,
        LocalDate dataOrdenha,
        LocalTime horaOrdenha,
        String turno,
        BigDecimal volumeLitros,
        Instant criadoEm,
        Instant atualizadoEm
) {
}
