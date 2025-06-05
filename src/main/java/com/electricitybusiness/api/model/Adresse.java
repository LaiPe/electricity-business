package com.electricitybusiness.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Entité représentant une adresse dans le système.
 * Une adresse peut être associée à un lieu et héberger plusieurs utilisateurs.
 */
@Entity
@Table(name = "adresse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_adresse")
    private Long numAdresse;

    @Column(name = "nom_adresse", length = 100)
    private String nomAdresse;

    @Column(name = "numero_et_rue", length = 200, nullable = false)
    @NotBlank(message = "Le numéro et rue sont obligatoires")
    private String numeroEtRue;

    @Column(name = "code_postal", length = 10, nullable = false)
    @NotBlank(message = "Le code postal est obligatoire")
    private String codePostal;

    @Column(name = "ville", length = 100, nullable = false)
    @NotBlank(message = "La ville est obligatoire")
    private String ville;

    @Column(name = "pays", length = 100, nullable = false)
    @NotBlank(message = "Le pays est obligatoire")
    private String pays;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "complement", length = 200)
    private String complement;

    @Column(name = "etage", length = 10)
    private String etage;

    @OneToOne
    @JoinColumn(name = "num_lieu")
    private Lieu lieu;
} 