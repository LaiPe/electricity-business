package com.electricitybusiness.api.controller;

import com.electricitybusiness.api.model.Utilisateur;
import com.electricitybusiness.api.service.UtilisateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 * Expose les endpoints pour les opérations CRUD sur les utilisateurs.
 */
@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    /**
     * Récupère tous les utilisateurs.
     * GET /api/utilisateurs
     */
    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.findAll();
        return ResponseEntity.ok(utilisateurs);
    }

    /**
     * Récupère un utilisateur par son ID.
     * GET /api/utilisateurs/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Long id) {
        return utilisateurService.findById(id)
                .map(utilisateur -> ResponseEntity.ok(utilisateur))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouvel utilisateur.
     * POST /api/utilisateurs
     */
    @PostMapping
    public ResponseEntity<Utilisateur> createUtilisateur(@Valid @RequestBody Utilisateur utilisateur) {
        Utilisateur savedUtilisateur = utilisateurService.save(utilisateur);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUtilisateur);
    }

    /**
     * Met à jour un utilisateur existant.
     * PUT /api/utilisateurs/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(@PathVariable Long id, @Valid @RequestBody Utilisateur utilisateur) {
        if (!utilisateurService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Utilisateur updatedUtilisateur = utilisateurService.update(id, utilisateur);
        return ResponseEntity.ok(updatedUtilisateur);
    }

    /**
     * Supprime un utilisateur.
     * DELETE /api/utilisateurs/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        if (!utilisateurService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        utilisateurService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 