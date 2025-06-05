package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.Adresse;
import com.electricitybusiness.api.repository.AdresseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des adresses.
 * Contient la logique métier pour les opérations CRUD sur les adresses.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AdresseService {

    private final AdresseRepository adresseRepository;

    /**
     * Récupère toutes les adresses.
     */
    @Transactional(readOnly = true)
    public List<Adresse> findAll() {
        return adresseRepository.findAll();
    }

    /**
     * Récupère une adresse par son ID.
     */
    @Transactional(readOnly = true)
    public Optional<Adresse> findById(Long id) {
        return adresseRepository.findById(id);
    }

    /**
     * Crée une nouvelle adresse.
     */
    public Adresse save(Adresse adresse) {
        return adresseRepository.save(adresse);
    }

    /**
     * Met à jour une adresse existante.
     */
    public Adresse update(Long id, Adresse adresse) {
        adresse.setNumAdresse(id);
        return adresseRepository.save(adresse);
    }

    /**
     * Supprime une adresse.
     */
    public void deleteById(Long id) {
        adresseRepository.deleteById(id);
    }

    /**
     * Vérifie si une adresse existe.
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return adresseRepository.existsById(id);
    }
} 