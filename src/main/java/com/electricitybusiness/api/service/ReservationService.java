package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.Reservation;
import com.electricitybusiness.api.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des réservations.
 * Contient la logique métier pour les opérations CRUD sur les réservations.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;

    /**
     * Récupère toutes les réservations.
     */
    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    /**
     * Récupère une réservation par son ID.
     */
    @Transactional(readOnly = true)
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    /**
     * Crée une nouvelle réservation.
     */
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    /**
     * Met à jour une réservation existante.
     */
    public Reservation update(Long id, Reservation reservation) {
        reservation.setNumReservation(id);
        return reservationRepository.save(reservation);
    }

    /**
     * Supprime une réservation.
     */
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    /**
     * Vérifie si une réservation existe.
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return reservationRepository.existsById(id);
    }
} 