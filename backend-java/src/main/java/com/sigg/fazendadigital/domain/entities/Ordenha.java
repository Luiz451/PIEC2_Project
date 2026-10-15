package com.sigg.fazendadigital.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "ordenha", uniqueConstraints = @UniqueConstraint(
        name = "uk_ordenha_animal_turno",
        columnNames = {"fazenda_id", "animal_id", "data_ordenha", "turno"}
))
@Getter
@Setter
@NoArgsConstructor
public class Ordenha {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "fazenda_id", nullable = false)
    private UUID fazendaId;

    @Column(name = "animal_id", nullable = false)
    private UUID animalId;

    @Column(name = "data_ordenha", nullable = false)
    private LocalDate dataOrdenha;

    @Column(name = "hora_ordenha", nullable = false)
    private LocalTime horaOrdenha;

    @Column(nullable = false, length = 40)
    private String turno;

    @Column(name = "volume_litros", nullable = false, precision = 10, scale = 3)
    private BigDecimal volumeLitros;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private Instant atualizadoEm;

    @PrePersist
    void aoCriar() {
        Instant agora = Instant.now();
        criadoEm = agora;
        atualizadoEm = agora;
    }

    @PreUpdate
    void aoAtualizar() {
        atualizadoEm = Instant.now();
    }
}
