package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.Utilisateur;
import com.electricitybusiness.api.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des utilisateurs.
 * Contient la logique métier pour les opérations CRUD sur les utilisateurs.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    /**
     * Récupère tous les utilisateurs.
     */
    @Transactional(readOnly = true)
    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }

    /**
     * Récupère un utilisateur par son ID.
     */
    @Transactional(readOnly = true)
    public Optional<Utilisateur> findById(Long id) {
        return utilisateurRepository.findById(id);
    }

    /**
     * Crée un nouveau utilisateur.
     */
    public Utilisateur save(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    /**
     * Met à jour un utilisateur existant.
     */
    public Utilisateur update(Long id, Utilisateur utilisateur) {
        utilisateur.setNumUtilisateur(id);
        return utilisateurRepository.save(utilisateur);
    }

    /**
     * Supprime un utilisateur.
     */
    public void deleteById(Long id) {
        utilisateurRepository.deleteById(id);
    }

    /**
     * Vérifie si un utilisateur existe.
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return utilisateurRepository.existsById(id);
    }
} 