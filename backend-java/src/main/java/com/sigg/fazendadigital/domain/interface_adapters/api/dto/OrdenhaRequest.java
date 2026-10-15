package com.sigg.fazendadigital.domain.interface_adapters.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record OrdenhaRequest(
        @NotNull UUID fazendaId,
        @NotNull UUID animalId,
        @NotNull LocalDate dataOrdenha,
        @NotNull LocalTime horaOrdenha,
        @NotBlank @Size(max = 40) String turno,
        @NotNull @DecimalMin(value = "0", inclusive = false) @Digits(integer = 7, fraction = 3)
        BigDecimal volumeLitros
) {
}
