package com.electricitybusiness.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entité représentant un utilisateur du système.
 * Un utilisateur peut effectuer des réservations et appartient à un lieu.
 */
@Entity
@Table(name = "utilisateur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_utilisateur")
    private Long numUtilisateur;

    @Column(name = "nom_utilisateur", length = 100, nullable = false)
    @NotBlank(message = "Le nom est obligatoire")
    private String nomUtilisateur;

    @Column(name = "prenom", length = 100, nullable = false)
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @Column(name = "pseudo", length = 50, nullable = false, unique = true)
    @NotBlank(message = "Le pseudo est obligatoire")
    private String pseudo;

    @Column(name = "mot_de_passe", length = 255, nullable = false)
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    @NotNull(message = "Le rôle est obligatoire")
    private RoleUtilisateur role;

    @Column(name = "adresse_mail", length = 150, nullable = false, unique = true)
    @Email(message = "L'adresse email doit être valide")
    @NotBlank(message = "L'adresse email est obligatoire")
    private String adresseMail;

    @Column(name = "date_de_naissance", nullable = false)
    @Past(message = "La date de naissance doit être dans le passé")
    @NotNull(message = "La date de naissance est obligatoire")
    private LocalDate dateDeNaissance;

    @Column(name = "iban", length = 34)
    private String iban;

    @Column(name = "vehicule", length = 100)
    private String vehicule;

    @Column(name = "banni", nullable = false)
    private Boolean banni = false;

    @Column(name = "compte_valide", nullable = false)
    private Boolean compteValide = false;

    @Column(name = "code_validation", length = 100)
    private String codeValidation;

    @Column(name = "date_inscription", nullable = false)
    private LocalDateTime dateInscription = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "num_lieu", nullable = false)
    // @NotNull(message = "Le lieu est obligatoire")
    @JsonBackReference("lieu-utilisateurs")
    private Lieu lieu;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("utilisateur-reservations")
    private List<Reservation> reservations;
} 