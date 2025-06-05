package com.electricitybusiness.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Entité représentant un lieu dans le système.
 * Un lieu peut contenir plusieurs bornes et être associé à une adresse.
 */
@Entity
@Table(name = "lieu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_lieu")
    private Long numLieu;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @OneToMany(mappedBy = "lieu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Borne> bornes;

    @OneToMany(mappedBy = "lieu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Utilisateur> utilisateurs;

    @OneToOne(mappedBy = "lieu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Adresse adresse;
} 