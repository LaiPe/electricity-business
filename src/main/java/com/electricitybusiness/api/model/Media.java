package com.electricitybusiness.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Entité représentant un média (image, vidéo, etc.) dans le système.
 * Un média peut illustrer plusieurs bornes.
 */
@Entity
@Table(name = "media")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_media")
    private Long numMedia;

    @Column(name = "url", length = 500, nullable = false)
    @NotBlank(message = "L'URL est obligatoire")
    private String url;

    @Column(name = "type", length = 50, nullable = false)
    @NotBlank(message = "Le type est obligatoire")
    private String type;

    @Column(name = "nom_media", length = 200, nullable = false)
    @NotBlank(message = "Le nom du média est obligatoire")
    private String nomMedia;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "taille")
    private Long taille;

    @ManyToMany(mappedBy = "medias", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Borne> bornes;
} 