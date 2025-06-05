package com.electricitybusiness.api.repository;

import com.electricitybusiness.api.model.Borne;
import com.electricitybusiness.api.model.EtatReservation;
import com.electricitybusiness.api.model.Reservation;
import com.electricitybusiness.api.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité Reservation.
 * Fournit les opérations CRUD et méthodes de recherche par utilisateur, borne et état.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    List<Reservation> findByUtilisateur(Utilisateur utilisateur);
    
    List<Reservation> findByBorne(Borne borne);
    
    List<Reservation> findByEtat(EtatReservation etat);
    
    List<Reservation> findByUtilisateurAndEtat(Utilisateur utilisateur, EtatReservation etat);
    
    List<Reservation> findByBorneAndEtat(Borne borne, EtatReservation etat);
} 