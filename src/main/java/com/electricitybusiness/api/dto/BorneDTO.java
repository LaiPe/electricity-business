package com.electricitybusiness.api.dto;

import com.electricitybusiness.api.model.EtatBorne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité Borne
 * Inclut une référence simple au lieu sans relations circulaires
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorneDTO {
    private Long numBorne;
    
    @NotBlank(message = "Le nom de la borne est obligatoire")
    private String nomBorne;
    
    @NotNull(message = "La latitude est obligatoire")
    @DecimalMin(value = "-90.0", message = "La latitude doit être comprise entre -90 et 90")
    @DecimalMax(value = "90.0", message = "La latitude doit être comprise entre -90 et 90")
    private BigDecimal latitude;
    
    @NotNull(message = "La longitude est obligatoire")
    @DecimalMin(value = "-180.0", message = "La longitude doit être comprise entre -180 et 180")
    @DecimalMax(value = "180.0", message = "La longitude doit être comprise entre -180 et 180")
    private BigDecimal longitude;
    
    @NotNull(message = "La puissance est obligatoire")
    @DecimalMin(value = "0.1", message = "La puissance doit être positive")
    private BigDecimal puissance;
    
    private String instruction;
    private Boolean surPied;
    
    @NotNull(message = "L'état de la borne est obligatoire")
    private EtatBorne etat;
    
    private Boolean occupee;
    private LocalDateTime dateCreation;
    private LocalDateTime derniereMaintenance;
    private LieuDTO lieu;
} 