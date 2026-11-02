package com.sigg.fazendadigital.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(
        name = "ordenha",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"animal_id", "data", "turno"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ordenha {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "animal-id")
    private Animal animal;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(nullable = false, length = 20)
    private String turno;

    @Column(name = "litros", nullable = false)
    private Double litros;

    @Column(columnDefinition = "TEXT")
    private String observacao;
}
