package com.humanbooster.electricity_business.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "utilisateur")
@Data
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 70)
    @Column(length = 70, nullable = false)
    private String nom;

    @NotBlank
    @Size(min = 2, max = 70)
    @Column(length = 70, nullable = false)
    private String prenom;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(length = 50, nullable = false)
    private String pseudo;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String motDePasse;

    @NotNull
    @Column(nullable = false)
    private Role role;

    private Date dateDeNaissance;

    @Size(min = 14, max = 34)
    @Column(length = 34)
    private String IBAN;

    @Size(min = 2, max = 50)
    @Column(length = 50)
    private String vehicule;

    @NotNull
    private Boolean banni;


}
