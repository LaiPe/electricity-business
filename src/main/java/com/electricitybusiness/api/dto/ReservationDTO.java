package com.electricitybusiness.api.dto;

import com.electricitybusiness.api.model.EtatReservation;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité Reservation
 * Inclut des références simples aux entités liées sans relations circulaires
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Long id;
    
    @NotNull(message = "La date de début est obligatoire")
    @Future(message = "La date de début doit être dans le futur")
    private LocalDateTime dateDebutReservation;
    
    @NotNull(message = "La date de fin est obligatoire")
    private LocalDateTime dateFinReservation;
    
    @NotNull(message = "L'état de la réservation est obligatoire")
    private EtatReservation etatReservation;
    
    private BigDecimal montantPaye;
    private LocalDateTime datePaiement;
    
    // Références simples sans relations bidirectionnelles
    @NotNull(message = "L'utilisateur est obligatoire")
    private Long numUtilisateur;
    
    @NotNull(message = "La borne est obligatoire")
    private Long numBorne;
} 