package com.electricitybusiness.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO pour l'entité Lieu
 * Représentation simplifiée sans relations circulaires
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LieuDTO {
    private Long numLieu;
    
    @NotBlank(message = "Les instructions sont obligatoires")
    private String instructions;
} 