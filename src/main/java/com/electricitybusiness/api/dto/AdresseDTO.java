package com.electricitybusiness.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO pour l'entité Adresse
 * Inclut une référence simple au lieu sans relations circulaires
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdresseDTO {
    private Long numAdresse;
    
    @NotBlank(message = "Le nom de l'adresse est obligatoire")
    private String nomAdresse;
    
    @NotBlank(message = "Le numéro et rue sont obligatoires")
    private String numeroEtRue;
    
    @NotBlank(message = "Le code postal est obligatoire")
    @Size(min = 5, max = 5, message = "Le code postal doit contenir 5 caractères")
    private String codePostal;
    
    @NotBlank(message = "La ville est obligatoire")
    private String ville;
    
    @NotBlank(message = "Le pays est obligatoire")
    private String pays;
    
    private String region;
    private String complement;
    private String etage;
    private LieuDTO lieu;
} 