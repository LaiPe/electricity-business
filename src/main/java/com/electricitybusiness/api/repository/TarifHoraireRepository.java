package com.electricitybusiness.api.repository;

import com.electricitybusiness.api.model.Borne;
import com.electricitybusiness.api.model.TarifHoraire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité TarifHoraire.
 * Fournit les opérations CRUD et méthodes de recherche par borne.
 */
@Repository
public interface TarifHoraireRepository extends JpaRepository<TarifHoraire, Long> {
    
    List<TarifHoraire> findByBorne(Borne borne);
    
    List<TarifHoraire> findByBorneAndActif(Borne borne, Boolean actif);
} 