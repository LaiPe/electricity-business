package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.Borne;
import com.electricitybusiness.api.repository.BorneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des bornes électriques.
 * Contient la logique métier pour les opérations CRUD sur les bornes.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BorneService {

    private final BorneRepository borneRepository;

    /**
     * Récupère toutes les bornes.
     */
    @Transactional(readOnly = true)
    public List<Borne> findAll() {
        return borneRepository.findAll();
    }

    /**
     * Récupère une borne par son ID.
     */
    @Transactional(readOnly = true)
    public Optional<Borne> findById(Long id) {
        return borneRepository.findById(id);
    }

    /**
     * Crée une nouvelle borne.
     */
    public Borne save(Borne borne) {
        return borneRepository.save(borne);
    }

    /**
     * Met à jour une borne existante.
     */
    public Borne update(Long id, Borne borne) {
        borne.setNumBorne(id);
        return borneRepository.save(borne);
    }

    /**
     * Supprime une borne.
     */
    public void deleteById(Long id) {
        borneRepository.deleteById(id);
    }

    /**
     * Vérifie si une borne existe.
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return borneRepository.existsById(id);
    }
} 