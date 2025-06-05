package com.electricitybusiness.api.repository;

import com.electricitybusiness.api.model.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité Adresse.
 * Fournit les opérations CRUD de base pour les adresses.
 */
@Repository
public interface AdresseRepository extends JpaRepository<Adresse, Long> {
} 