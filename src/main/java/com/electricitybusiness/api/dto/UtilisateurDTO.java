package com.electricitybusiness.api.dto;

import com.electricitybusiness.api.model.RoleUtilisateur;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité Utilisateur
 * Exclut le mot de passe et évite les relations circulaires
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDTO {
    private Long numUtilisateur;
    
    @NotBlank(message = "Le nom de l'utilisateur est obligatoire")
    private String nomUtilisateur;
    
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;
    
    @NotBlank(message = "Le pseudo est obligatoire")
    private String pseudo;
    
    @NotNull(message = "Le rôle est obligatoire")
    private RoleUtilisateur role;
    
    @NotBlank(message = "L'adresse email est obligatoire")
    @Email(message = "L'adresse email doit être valide")
    private String adresseMail;
    
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateDeNaissance;
    
    private String iban;
    private String vehicule;
    private Boolean banni;
    private Boolean compteValide;
    private LocalDateTime dateInscription;
    private LieuDTO lieu;
} 