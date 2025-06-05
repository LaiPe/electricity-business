package com.electricitybusiness.api.utils;

import com.electricitybusiness.api.dto.BorneDTO;
import com.electricitybusiness.api.mapper.EntityMapper;
import com.electricitybusiness.api.model.Borne;
import com.electricitybusiness.api.model.EtatReservation;
import com.electricitybusiness.api.model.Reservation;
import com.electricitybusiness.api.service.BorneService;
import com.electricitybusiness.api.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BorneLocalisator {
    
    private static BorneService borneService;
    private static ReservationService reservationService;
    private static EntityMapper entityMapper;

    @Autowired
    public void setBorneService(BorneService borneService) {
        BorneLocalisator.borneService = borneService;
    }

    @Autowired
    public void setReservationService(ReservationService reservationService) {
        BorneLocalisator.reservationService = reservationService;
    }

    @Autowired
    public void setEntityMapper(EntityMapper entityMapper) {
        BorneLocalisator.entityMapper = entityMapper;
    }

    /**
     * Trouve toutes les bornes dans un rayon donné autour d'un point géographique
     * @param longitude Longitude du point central
     * @param latitude Latitude du point central
     * @param rayon Rayon de recherche en kilomètres
     * @return Liste des bornes trouvées dans le rayon spécifié
     */
    public static List<BorneDTO> get_nearby_borne(BigDecimal longitude, BigDecimal latitude, double rayon) {
        // TODO: Implémenter la logique de recherche des bornes dans le rayon
        // Utiliser la formule de Haversine pour calculer la distance
        return List.of();
    }

    /**
     * Trouve toutes les bornes libres à un moment donné
     * @param time Moment pour lequel on cherche les bornes libres
     * @return Liste des bornes libres au moment spécifié
     */
    public static List<BorneDTO> get_free_borne(LocalDateTime time) {
        if (time == null) {
            throw new IllegalArgumentException("Le temps ne peut pas être null");
        }

        List<Borne> allBornes = borneService.findAll();
        List<Reservation> activeReservations = reservationService.findAll().stream()
            .filter(r -> r.getEtat() == EtatReservation.ACCEPTEE)
            .filter(r -> time.isAfter(r.getDateDebut()) && time.isBefore(r.getDateFin()))
            .collect(Collectors.toList());

        return allBornes.stream()
            .filter(borne -> activeReservations.stream()
                .noneMatch(reservation -> reservation.getBorne().getNumBorne().equals(borne.getNumBorne())))
            .map(entityMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Trouve toutes les bornes libres dans un rayon donné autour d'un point géographique à un moment donné
     * @param longitude Longitude du point central
     * @param latitude Latitude du point central
     * @param time Moment pour lequel on cherche les bornes libres
     * @return Liste des bornes libres trouvées dans le rayon spécifié au moment donné
     */
    public static List<BorneDTO> get_free_nearby_borne(BigDecimal longitude, BigDecimal latitude, double rayon, LocalDateTime time) {
        // TODO: Implémenter la logique de recherche des bornes libres dans le rayon
        return List.of();
    }
}
