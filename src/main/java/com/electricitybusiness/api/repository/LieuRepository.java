package com.electricitybusiness.api.repository;

import com.electricitybusiness.api.model.Lieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité Lieu.
 * Fournit les opérations CRUD de base pour les lieux.
 */
@Repository
public interface LieuRepository extends JpaRepository<Lieu, Long> {
} 