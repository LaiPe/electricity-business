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

    public static double calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            throw new IllegalArgumentException("Les coordonnées ne peuvent pas être null");
        }
        if (lat1.doubleValue() < -90 || lat1.doubleValue() > 90 || 
            lat2.doubleValue() < -90 || lat2.doubleValue() > 90) {
            throw new IllegalArgumentException("La latitude doit être comprise entre -90 et 90");
        }
        if (lon1.doubleValue() < -180 || lon1.doubleValue() > 180 || 
            lon2.doubleValue() < -180 || lon2.doubleValue() > 180) {
            throw new IllegalArgumentException("La longitude doit être comprise entre -180 et 180");
        }

        final int R = 6371; // Rayon de la Terre en km

        double latDistance = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double lonDistance = Math.toRadians(lon2.doubleValue() - lon1.doubleValue());
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1.doubleValue())) * Math.cos(Math.toRadians(lat2.doubleValue()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * Trouve toutes les bornes dans un rayon donné autour d'un point géographique
     * @param longitude Longitude du point central
     * @param latitude Latitude du point central
     * @param rayon Rayon de recherche en kilomètres
     * @return Liste des bornes trouvées dans le rayon spécifié
     */
    public static List<BorneDTO> get_nearby_borne(BigDecimal longitude, BigDecimal latitude, double rayon) {
        if (longitude == null || latitude == null) {
            throw new IllegalArgumentException("Les coordonnées ne peuvent pas être null");
        }
        if (longitude.doubleValue() < -180 || longitude.doubleValue() > 180) {
            throw new IllegalArgumentException("La longitude doit être comprise entre -180 et 180");
        }
        if (latitude.doubleValue() < -90 || latitude.doubleValue() > 90) {
            throw new IllegalArgumentException("La latitude doit être comprise entre -90 et 90");
        }

        return borneService.findAll().stream()
            .filter(borne -> calculateDistance(latitude, longitude, borne.getLatitude(), borne.getLongitude()) <= rayon)
            .map(entityMapper::toDTO)
            .collect(Collectors.toList());
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
        if (time == null) {
            throw new IllegalArgumentException("Le temps ne peut pas être null");
        }
        if (longitude == null || latitude == null) {
            throw new IllegalArgumentException("Les coordonnées ne peuvent pas être null");
        }
        if (longitude.doubleValue() < -180 || longitude.doubleValue() > 180) {
            throw new IllegalArgumentException("La longitude doit être comprise entre -180 et 180");
        }
        if (latitude.doubleValue() < -90 || latitude.doubleValue() > 90) {
            throw new IllegalArgumentException("La latitude doit être comprise entre -90 et 90");
        }

        List<Borne> allBornes = borneService.findAll();
        List<Reservation> activeReservations = reservationService.findAll().stream()
            .filter(r -> r.getEtat() == EtatReservation.ACCEPTEE)
            .filter(r -> time.isAfter(r.getDateDebut()) && time.isBefore(r.getDateFin()))
            .collect(Collectors.toList());

        return allBornes.stream()
            .filter(borne -> calculateDistance(latitude, longitude, borne.getLatitude(), borne.getLongitude()) <= rayon)
            .filter(borne -> activeReservations.stream()
                .noneMatch(reservation -> reservation.getBorne().getNumBorne().equals(borne.getNumBorne())))
            .map(entityMapper::toDTO)
            .collect(Collectors.toList());
    }
}
