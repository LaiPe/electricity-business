package com.electricitybusiness.api.controller;

import com.electricitybusiness.api.model.Media;
import com.electricitybusiness.api.service.MediaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des médias.
 * Expose les endpoints pour les opérations CRUD sur les médias.
 */
@RestController
@RequestMapping("/api/medias")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    /**
     * Récupère tous les médias.
     * GET /api/medias
     */
    @GetMapping
    public ResponseEntity<List<Media>> getAllMedias() {
        List<Media> medias = mediaService.findAll();
        return ResponseEntity.ok(medias);
    }

    /**
     * Récupère un média par son ID.
     * GET /api/medias/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Media> getMediaById(@PathVariable Long id) {
        return mediaService.findById(id)
                .map(media -> ResponseEntity.ok(media))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouveau média.
     * POST /api/medias
     */
    @PostMapping
    public ResponseEntity<Media> createMedia(@Valid @RequestBody Media media) {
        Media savedMedia = mediaService.save(media);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMedia);
    }

    /**
     * Met à jour un média existant.
     * PUT /api/medias/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Media> updateMedia(@PathVariable Long id, @Valid @RequestBody Media media) {
        if (!mediaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Media updatedMedia = mediaService.update(id, media);
        return ResponseEntity.ok(updatedMedia);
    }

    /**
     * Supprime un média.
     * DELETE /api/medias/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
        if (!mediaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        mediaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 