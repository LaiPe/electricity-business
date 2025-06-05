package com.electricitybusiness.api.controller;

import com.electricitybusiness.api.dto.AdresseDTO;
import com.electricitybusiness.api.mapper.EntityMapper;
import com.electricitybusiness.api.model.Adresse;
import com.electricitybusiness.api.service.AdresseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur REST pour la gestion des adresses.
 * Expose les endpoints pour les opérations CRUD sur les adresses.
 * Utilise des DTOs pour éviter les références circulaires.
 */
@RestController
@RequestMapping("/api/adresses")
@RequiredArgsConstructor
public class AdresseController {

    private final AdresseService adresseService;
    private final EntityMapper mapper;

    /**
     * Récupère toutes les adresses.
     * GET /api/adresses
     */
    @GetMapping
    public ResponseEntity<List<AdresseDTO>> getAllAdresses() {
        List<Adresse> adresses = adresseService.findAll();
        List<AdresseDTO> adressesDTO = adresses.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(adressesDTO);
    }

    /**
     * Récupère une adresse par son ID.
     * GET /api/adresses/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdresseDTO> getAdresseById(@PathVariable Long id) {
        return adresseService.findById(id)
                .map(adresse -> ResponseEntity.ok(mapper.toDTO(adresse)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée une nouvelle adresse.
     * POST /api/adresses
     */
    @PostMapping
    public ResponseEntity<AdresseDTO> createAdresse(@Valid @RequestBody AdresseDTO adresseDTO) {
        Adresse adresse = mapper.toEntity(adresseDTO);
        Adresse savedAdresse = adresseService.save(adresse);
        AdresseDTO savedDTO = mapper.toDTO(savedAdresse);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDTO);
    }

    /**
     * Met à jour une adresse existante.
     * PUT /api/adresses/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdresseDTO> updateAdresse(@PathVariable Long id, @Valid @RequestBody AdresseDTO adresseDTO) {
        if (!adresseService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Adresse adresse = mapper.toEntity(adresseDTO);
        Adresse updatedAdresse = adresseService.update(id, adresse);
        AdresseDTO updatedDTO = mapper.toDTO(updatedAdresse);
        return ResponseEntity.ok(updatedDTO);
    }

    /**
     * Supprime une adresse.
     * DELETE /api/adresses/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdresse(@PathVariable Long id) {
        if (!adresseService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        adresseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 