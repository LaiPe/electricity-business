package com.electricitybusiness.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entité représentant une réservation de borne électrique.
 * Table de liaison entre Utilisateur et Borne avec attributs métier.
 */
@Entity
@Table(name = "reserver")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_reservation")
    private Long numReservation;

    @Column(name = "date_debut", nullable = false)
    @NotNull(message = "La date de début est obligatoire")
    @Future(message = "La date de début doit être dans le futur")
    private LocalDateTime dateDebut;

    @Column(name = "date_fin", nullable = false)
    @NotNull(message = "La date de fin est obligatoire")
    private LocalDateTime dateFin;

    @Column(name = "prix_minute_histo", precision = 10, scale = 4, nullable = false)
    @DecimalMin(value = "0.0", message = "Le prix par minute doit être positif")
    @NotNull(message = "Le prix par minute historique est obligatoire")
    private BigDecimal prixMinuteHisto;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat", length = 20, nullable = false)
    @NotNull(message = "L'état est obligatoire")
    private EtatReservation etat;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    @Column(name = "montant_total", precision = 10, scale = 2)
    private BigDecimal montantTotal;

    @Column(name = "recu_genere", nullable = false)
    private Boolean recuGenere = false;

    @ManyToOne
    @JoinColumn(name = "num_utilisateur", nullable = false)
    @NotNull(message = "L'utilisateur est obligatoire")
    @JsonBackReference("utilisateur-reservations")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "num_borne", nullable = false)
    @NotNull(message = "La borne est obligatoire")
    @JsonBackReference("borne-reservations")
    private Borne borne;
} 