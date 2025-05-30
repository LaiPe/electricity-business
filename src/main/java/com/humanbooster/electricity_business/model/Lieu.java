package com.humanbooster.electricity_business.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "lieu")
@Data
public class Lieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Size(min = 2, max = 3000)
    @Column(length = 3000)
    private String instructions;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @OneToOne(targetEntity = AdresseLieu.class, mappedBy = "lieu")
    private AdresseLieu adresse;
}
