package com.electricitybusiness.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * DTO pour l'entité Media
 * Représentation simplifiée sans relations circulaires
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTO {
    private Long numMedia;
    
    @NotBlank(message = "Le nom du média est obligatoire")
    private String nomMedia;
    
    @NotBlank(message = "Le type de média est obligatoire")
    private String typeMedia;
    
    @NotBlank(message = "L'URL du média est obligatoire")
    private String urlMedia;
    
    private String description;
    private LocalDateTime dateCreation;
} 