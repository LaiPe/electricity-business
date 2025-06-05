package com.electricitybusiness.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entité représentant une borne électrique dans le système.
 * Une borne appartient à un lieu et peut avoir des réservations et des tarifs.
 */
@Entity
@Table(name = "borne")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Borne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_borne")
    private Long numBorne;

    @Column(name = "nom_borne", length = 100, nullable = false)
    @NotBlank(message = "Le nom de la borne est obligatoire")
    private String nomBorne;

    @Column(name = "latitude", precision = 10, scale = 8, nullable = false)
    @DecimalMin(value = "-90.0", message = "La latitude doit être entre -90 et 90")
    @DecimalMax(value = "90.0", message = "La latitude doit être entre -90 et 90")
    @NotNull(message = "La latitude est obligatoire")
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8, nullable = false)
    @DecimalMin(value = "-180.0", message = "La longitude doit être entre -180 et 180")
    @DecimalMax(value = "180.0", message = "La longitude doit être entre -180 et 180")
    @NotNull(message = "La longitude est obligatoire")
    private BigDecimal longitude;

    @Column(name = "puissance", precision = 10, scale = 2, nullable = false)
    @DecimalMin(value = "0.0", message = "La puissance doit être positive")
    @NotNull(message = "La puissance est obligatoire")
    private BigDecimal puissance;

    @Column(name = "instruction", columnDefinition = "TEXT")
    private String instruction;

    @Column(name = "sur_pied", nullable = false)
    private Boolean surPied = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat", length = 20, nullable = false)
    @NotNull(message = "L'état est obligatoire")
    private EtatBorne etat;

    @Column(name = "occupee", nullable = false)
    private Boolean occupee = false;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "derniere_maintenance")
    private LocalDateTime derniereMaintenance;

    @ManyToOne
    @JoinColumn(name = "num_lieu", nullable = false)
    @NotNull(message = "Le lieu est obligatoire")
    @JsonBackReference("lieu-bornes")
    private Lieu lieu;

    @OneToMany(mappedBy = "borne", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("borne-tarifs")
    private List<TarifHoraire> tarifsHoraires;

    @OneToMany(mappedBy = "borne", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("borne-reservations")
    private List<Reservation> reservations;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "illustrer",
        joinColumns = @JoinColumn(name = "num_borne"),
        inverseJoinColumns = @JoinColumn(name = "num_media")
    )
    @JsonIgnore
    private List<Media> medias;
} 