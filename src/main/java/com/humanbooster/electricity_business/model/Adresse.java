package com.humanbooster.electricity_business.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name="adresse")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "usage_adresse")
@Data
public abstract class Adresse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 200)
    @Column(length = 200, nullable = false)
    private String rue;

    @Size(min = 2, max = 300)
    @Column(length = 300)
    private String complement;

    @NotBlank
    @Size(min = 2, max = 30)
    @Column(length = 30, nullable = false)
    private String codePostal;

    @NotBlank
    @Size(min = 2, max = 200)
    @Column(length = 200, nullable = false)
    private String ville;

    @Size(min = 2, max = 200)
    @Column(length = 200)
    private String region;

    @NotBlank
    @Size(min = 2, max = 200)
    @Column(length = 200, nullable = false)
    private String pays;
}
