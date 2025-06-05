package com.electricitybusiness.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entité représentant un tarif horaire pour une borne électrique.
 * Permet une tarification flexible selon les heures, jours et périodes.
 */
@Entity
@Table(name = "tarif_horaire")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifHoraire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_tarif")
    private Long numTarif;

    @Column(name = "tarif_par_minute", precision = 10, scale = 4, nullable = false)
    @DecimalMin(value = "0.0", message = "Le tarif par minute doit être positif")
    @NotNull(message = "Le tarif par minute est obligatoire")
    private BigDecimal tarifParMinute;

    @Column(name = "heure_debut", nullable = false)
    @NotNull(message = "L'heure de début est obligatoire")
    private LocalTime heureDebut;

    @Column(name = "heure_fin", nullable = false)
    @NotNull(message = "L'heure de fin est obligatoire")
    private LocalTime heureFin;

    @Column(name = "jour_semaine")
    @Min(value = 1, message = "Le jour de la semaine doit être entre 1 et 7")
    @Max(value = 7, message = "Le jour de la semaine doit être entre 1 et 7")
    private Integer jourSemaine; // 1=Lundi, 7=Dimanche, NULL=tous les jours

    @Column(name = "date_debut", nullable = false)
    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "actif", nullable = false)
    private Boolean actif = true;

    @ManyToOne
    @JoinColumn(name = "num_borne", nullable = false)
    @NotNull(message = "La borne est obligatoire")
    @JsonBackReference("borne-tarifs")
    private Borne borne;
} 