package com.electricitybusiness.api.controller;

import com.electricitybusiness.api.model.Borne;
import com.electricitybusiness.api.service.BorneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des bornes électriques.
 * Expose les endpoints pour les opérations CRUD sur les bornes.
 */
@RestController
@RequestMapping("/api/bornes")
@RequiredArgsConstructor
public class BorneController {

    private final BorneService borneService;

    /**
     * Récupère toutes les bornes.
     * GET /api/bornes
     * Accessible par tous les utilisateurs authentifiés
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Borne>> getAllBornes() {
        List<Borne> bornes = borneService.findAll();
        return ResponseEntity.ok(bornes);
    }

    /**
     * Récupère une borne par son ID.
     * GET /api/bornes/{id}
     * Accessible par tous les utilisateurs authentifiés
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Borne> getBorneById(@PathVariable Long id) {
        return borneService.findById(id)
                .map(borne -> ResponseEntity.ok(borne))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée une nouvelle borne.
     * POST /api/bornes
     * Accessible par les ADMIN et PROPRIETAIRE
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROPRIETAIRE')")
    public ResponseEntity<Borne> createBorne(@Valid @RequestBody Borne borne) {
        Borne savedBorne = borneService.save(borne);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBorne);
    }

    /**
     * Met à jour une borne existante.
     * PUT /api/bornes/{id}
     * Accessible par les ADMIN et PROPRIETAIRE
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROPRIETAIRE')")
    public ResponseEntity<Borne> updateBorne(@PathVariable Long id, @Valid @RequestBody Borne borne) {
        if (!borneService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Borne updatedBorne = borneService.update(id, borne);
        return ResponseEntity.ok(updatedBorne);
    }

    /**
     * Supprime une borne.
     * DELETE /api/bornes/{id}
     * Accessible uniquement par les ADMIN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBorne(@PathVariable Long id) {
        if (!borneService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        borneService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 