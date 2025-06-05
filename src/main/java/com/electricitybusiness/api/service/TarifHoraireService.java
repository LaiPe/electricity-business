package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.TarifHoraire;
import com.electricitybusiness.api.repository.TarifHoraireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des tarifs horaires.
 * Contient la logique métier pour les opérations CRUD sur les tarifs horaires.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TarifHoraireService {

    private final TarifHoraireRepository tarifHoraireRepository;

    /**
     * Récupère tous les tarifs horaires.
     */
    @Transactional(readOnly = true)
    public List<TarifHoraire> findAll() {
        return tarifHoraireRepository.findAll();
    }

    /**
     * Récupère un tarif horaire par son ID.
     */
    @Transactional(readOnly = true)
    public Optional<TarifHoraire> findById(Long id) {
        return tarifHoraireRepository.findById(id);
    }

    /**
     * Crée un nouveau tarif horaire.
     */
    public TarifHoraire save(TarifHoraire tarifHoraire) {
        return tarifHoraireRepository.save(tarifHoraire);
    }

    /**
     * Met à jour un tarif horaire existant.
     */
    public TarifHoraire update(Long id, TarifHoraire tarifHoraire) {
        tarifHoraire.setNumTarif(id);
        return tarifHoraireRepository.save(tarifHoraire);
    }

    /**
     * Supprime un tarif horaire.
     */
    public void deleteById(Long id) {
        tarifHoraireRepository.deleteById(id);
    }

    /**
     * Vérifie si un tarif horaire existe.
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return tarifHoraireRepository.existsById(id);
    }
} 