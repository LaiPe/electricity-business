package com.electricitybusiness.api.controller;

import com.electricitybusiness.api.model.TarifHoraire;
import com.electricitybusiness.api.service.TarifHoraireService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des tarifs horaires.
 * Expose les endpoints pour les opérations CRUD sur les tarifs horaires.
 */
@RestController
@RequestMapping("/api/tarifs-horaires")
@RequiredArgsConstructor
public class TarifHoraireController {

    private final TarifHoraireService tarifHoraireService;

    /**
     * Récupère tous les tarifs horaires.
     * GET /api/tarifs-horaires
     */
    @GetMapping
    public ResponseEntity<List<TarifHoraire>> getAllTarifsHoraires() {
        List<TarifHoraire> tarifsHoraires = tarifHoraireService.findAll();
        return ResponseEntity.ok(tarifsHoraires);
    }

    /**
     * Récupère un tarif horaire par son ID.
     * GET /api/tarifs-horaires/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TarifHoraire> getTarifHoraireById(@PathVariable Long id) {
        return tarifHoraireService.findById(id)
                .map(tarifHoraire -> ResponseEntity.ok(tarifHoraire))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouveau tarif horaire.
     * POST /api/tarifs-horaires
     */
    @PostMapping
    public ResponseEntity<TarifHoraire> createTarifHoraire(@Valid @RequestBody TarifHoraire tarifHoraire) {
        TarifHoraire savedTarifHoraire = tarifHoraireService.save(tarifHoraire);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTarifHoraire);
    }

    /**
     * Met à jour un tarif horaire existant.
     * PUT /api/tarifs-horaires/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TarifHoraire> updateTarifHoraire(@PathVariable Long id, @Valid @RequestBody TarifHoraire tarifHoraire) {
        if (!tarifHoraireService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        TarifHoraire updatedTarifHoraire = tarifHoraireService.update(id, tarifHoraire);
        return ResponseEntity.ok(updatedTarifHoraire);
    }

    /**
     * Supprime un tarif horaire.
     * DELETE /api/tarifs-horaires/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarifHoraire(@PathVariable Long id) {
        if (!tarifHoraireService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        tarifHoraireService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 