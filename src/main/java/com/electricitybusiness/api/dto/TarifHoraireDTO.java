package com.electricitybusiness.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO pour l'entité TarifHoraire
 * Inclut une référence simple à la borne sans relations circulaires
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifHoraireDTO {
    private Long numTarif;
    
    @NotNull(message = "Le tarif par minute est obligatoire")
    @DecimalMin(value = "0.0", message = "Le tarif doit être positif")
    private BigDecimal tarifParMinute;
    
    @NotNull(message = "L'heure de début est obligatoire")
    private LocalTime heureDebut;
    
    @NotNull(message = "L'heure de fin est obligatoire")
    private LocalTime heureFin;
    
    @Min(value = 1, message = "Le jour de la semaine doit être entre 1 et 7")
    @Max(value = 7, message = "Le jour de la semaine doit être entre 1 et 7")
    private Integer jourSemaine; // 1=Lundi, 7=Dimanche, NULL=tous les jours
    
    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;
    
    private LocalDate dateFin;
    private Boolean actif;
    
    // Référence simple sans relation bidirectionnelle
    @NotNull(message = "La borne est obligatoire")
    private Long numBorne;
} 