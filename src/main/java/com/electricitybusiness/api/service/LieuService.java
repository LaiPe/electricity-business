package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.Lieu;
import com.electricitybusiness.api.repository.LieuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des lieux.
 * Contient la logique métier pour les opérations sur les lieux.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class LieuService {

    private final LieuRepository lieuRepository;

    /**
     * Récupère tous les lieux.
     */
    @Transactional(readOnly = true)
    public List<Lieu> findAll() {
        return lieuRepository.findAll();
    }

    /**
     * Récupère un lieu par son ID.
     */
    @Transactional(readOnly = true)
    public Optional<Lieu> findById(Long id) {
        return lieuRepository.findById(id);
    }

    /**
     * Crée un nouveau lieu.
     */
    public Lieu save(Lieu lieu) {
        return lieuRepository.save(lieu);
    }

    /**
     * Met à jour un lieu existant.
     */
    public Lieu update(Long id, Lieu lieu) {
        lieu.setNumLieu(id);
        return lieuRepository.save(lieu);
    }

    /**
     * Supprime un lieu.
     */
    public void deleteById(Long id) {
        lieuRepository.deleteById(id);
    }

    /**
     * Vérifie si un lieu existe.
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return lieuRepository.existsById(id);
    }
} 