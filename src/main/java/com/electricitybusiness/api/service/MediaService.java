package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.Media;
import com.electricitybusiness.api.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des médias.
 * Contient la logique métier pour les opérations CRUD sur les médias.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MediaService {

    private final MediaRepository mediaRepository;

    /**
     * Récupère tous les médias.
     */
    @Transactional(readOnly = true)
    public List<Media> findAll() {
        return mediaRepository.findAll();
    }

    /**
     * Récupère un média par son ID.
     */
    @Transactional(readOnly = true)
    public Optional<Media> findById(Long id) {
        return mediaRepository.findById(id);
    }

    /**
     * Crée un nouveau média.
     */
    public Media save(Media media) {
        return mediaRepository.save(media);
    }

    /**
     * Met à jour un média existant.
     */
    public Media update(Long id, Media media) {
        media.setNumMedia(id);
        return mediaRepository.save(media);
    }

    /**
     * Supprime un média.
     */
    public void deleteById(Long id) {
        mediaRepository.deleteById(id);
    }

    /**
     * Vérifie si un média existe.
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return mediaRepository.existsById(id);
    }
} 