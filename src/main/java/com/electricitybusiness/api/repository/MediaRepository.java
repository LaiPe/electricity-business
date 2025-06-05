package com.electricitybusiness.api.repository;

import com.electricitybusiness.api.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité Media.
 * Fournit les opérations CRUD de base pour les médias.
 */
@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    
    List<Media> findByType(String type);
} 