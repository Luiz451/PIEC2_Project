package com.sigg.fazendadigital.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "animal")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "fazenda_id")
    private UUID fazendaId;

    @Column(nullable = false, length = 50)
    private String brinco;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, length = 100)
    private String raca;

    @Column(name = "data_nasc")
    private LocalDate dataNascimento;

    @Column(length = 20)
    private String sexo;

    @Column(length = 50)
    private String status;

    @Column(name = "foto_url", columnDefinition = "TEXT")
    private String fotoUrl;
}