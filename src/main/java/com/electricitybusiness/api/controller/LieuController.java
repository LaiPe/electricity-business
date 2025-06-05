package com.electricitybusiness.api.controller;

import com.electricitybusiness.api.dto.LieuDTO;
import com.electricitybusiness.api.mapper.EntityMapper;
import com.electricitybusiness.api.model.Lieu;
import com.electricitybusiness.api.service.LieuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur REST pour la gestion des lieux.
 * Expose les endpoints pour les opérations CRUD sur les lieux.
 * Utilise des DTOs pour éviter les références circulaires.
 */
@RestController
@RequestMapping("/api/lieux")
@RequiredArgsConstructor
public class LieuController {

    private final LieuService lieuService;
    private final EntityMapper mapper;

    /**
     * Récupère tous les lieux.
     * GET /api/lieux
     */
    @GetMapping
    public ResponseEntity<List<LieuDTO>> getAllLieux() {
        List<Lieu> lieux = lieuService.findAll();
        List<LieuDTO> lieuxDTO = lieux.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lieuxDTO);
    }

    /**
     * Récupère un lieu par son ID.
     * GET /api/lieux/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<LieuDTO> getLieuById(@PathVariable Long id) {
        return lieuService.findById(id)
                .map(lieu -> ResponseEntity.ok(mapper.toDTO(lieu)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouveau lieu.
     * POST /api/lieux
     */
    @PostMapping
    public ResponseEntity<LieuDTO> createLieu(@Valid @RequestBody LieuDTO lieuDTO) {
        Lieu lieu = mapper.toEntity(lieuDTO);
        Lieu savedLieu = lieuService.save(lieu);
        LieuDTO savedDTO = mapper.toDTO(savedLieu);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDTO);
    }

    /**
     * Met à jour un lieu existant.
     * PUT /api/lieux/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<LieuDTO> updateLieu(@PathVariable Long id, @Valid @RequestBody LieuDTO lieuDTO) {
        if (!lieuService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Lieu lieu = mapper.toEntity(lieuDTO);
        Lieu updatedLieu = lieuService.update(id, lieu);
        LieuDTO updatedDTO = mapper.toDTO(updatedLieu);
        return ResponseEntity.ok(updatedDTO);
    }

    /**
     * Supprime un lieu.
     * DELETE /api/lieux/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLieu(@PathVariable Long id) {
        if (!lieuService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        lieuService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 