package com.electricitybusiness.api.repository;

import com.electricitybusiness.api.model.Borne;
import com.electricitybusiness.api.model.EtatBorne;
import com.electricitybusiness.api.model.Lieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité Borne.
 * Fournit les opérations CRUD et méthodes de recherche par lieu et état.
 */
@Repository
public interface BorneRepository extends JpaRepository<Borne, Long> {
    
    List<Borne> findByLieu(Lieu lieu);
    
    List<Borne> findByEtat(EtatBorne etat);
    
    List<Borne> findByOccupee(Boolean occupee);
    
    List<Borne> findByLieuAndEtat(Lieu lieu, EtatBorne etat);
} 