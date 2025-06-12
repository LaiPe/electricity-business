package com.electricitybusiness.api.utils;

import com.electricitybusiness.api.dto.BorneDTO;
import com.electricitybusiness.api.mapper.EntityMapper;
import com.electricitybusiness.api.model.*;
import com.electricitybusiness.api.service.BorneService;
import com.electricitybusiness.api.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorneLocalisatorTest {

    @Mock
    private BorneService borneService;

    @Mock
    private ReservationService reservationService;

    private EntityMapper entityMapper;
    private BorneLocalisator borneLocalisator;
    
    private Borne testBorne;
    private Reservation testReservation1;
    private Reservation testReservation2;
    private BigDecimal testLongitude;
    private BigDecimal testLatitude;
    private double testRayon;
    private LocalDateTime testTime;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entityMapper = new EntityMapper();
        borneLocalisator = new BorneLocalisator(borneService, reservationService, entityMapper);

        // Initialisation des données de test
        testLongitude = new BigDecimal("2.3522"); // Paris longitude
        testLatitude = new BigDecimal("48.8566"); // Paris latitude
        testRayon = 5.0; // 5 kilomètres
        testTime = LocalDateTime.now();

        // Création de la borne de test
        testBorne = new Borne();
        testBorne.setNumBorne(1L);
        testBorne.setNomBorne("Borne de test");
        testBorne.setLatitude(testLatitude);
        testBorne.setLongitude(testLongitude);
        testBorne.setPuissance(new BigDecimal("50.0"));
        testBorne.setInstruction("Instructions de test");
        testBorne.setSurPied(true);
        testBorne.setEtat(EtatBorne.ACTIVE);
        testBorne.setOccupee(false);

        // Création des réservations de test
        testReservation1 = new Reservation();
        testReservation1.setNumReservation(1L);
        testReservation1.setDateDebut(testTime.minusDays(2));
        testReservation1.setDateFin(testTime.minusDays(1));
        testReservation1.setEtat(EtatReservation.TERMINEE);
        testReservation1.setBorne(testBorne);

        testReservation2 = new Reservation();
        testReservation2.setNumReservation(2L);
        testReservation2.setDateDebut(testTime.plusDays(1));
        testReservation2.setDateFin(testTime.plusDays(2));
        testReservation2.setEtat(EtatReservation.ACCEPTEE);
        testReservation2.setBorne(testBorne);
    }


    // Tests de la méthode calculateDistance
    @ParameterizedTest(name = "Distance entre ({0}, {1}) et ({2}, {3}) devrait être {4} km (±{5} km)")
    @CsvSource({
        "48.8566, 2.3522, 48.8566, 2.3522, 0.0, 0.001", // Même point (Paris)
        "48.8566, 2.3522, 45.7578, 4.8320, 392.0, 1.0", // Paris-Lyon
        "0.0, 0.0, 0.0, 1.0, 111.2, 0.1", // 1 degré de longitude à l'équateur
        "0.0, 0.0, 1.0, 0.0, 111.2, 0.1", // 1 degré de latitude
        "48.8566, 2.3522, 40.7128, -74.0060, 5837.0, 1.0" // Paris-New York
    })
    void testCalculateDistance(String lat1Str, String lon1Str, String lat2Str, String lon2Str, 
                             double expectedDistance, double tolerance) {
        // Arrange
        BigDecimal lat1 = new BigDecimal(lat1Str);
        BigDecimal lon1 = new BigDecimal(lon1Str);
        BigDecimal lat2 = new BigDecimal(lat2Str);
        BigDecimal lon2 = new BigDecimal(lon2Str);

        // Act
        double distance = borneLocalisator.calculateDistance(lat1, lon1, lat2, lon2);

        // Assert
        assertEquals(expectedDistance, distance, tolerance, 
            String.format("Distance incorrecte entre (%s, %s) et (%s, %s)", 
                lat1Str, lon1Str, lat2Str, lon2Str));
    }

    @ParameterizedTest(name = "Coordonnées invalides: lat1={0}, lon1={1}, lat2={2}, lon2={3}")
    @CsvSource({
        "91.0, 0.0, 0.0, 0.0", // Latitude trop grande
        "-91.0, 0.0, 0.0, 0.0", // Latitude trop petite
        "0.0, 181.0, 0.0, 0.0", // Longitude trop grande
        "0.0, -181.0, 0.0, 0.0", // Longitude trop petite
        "null, 0.0, 0.0, 0.0", // Latitude null
        "0.0, null, 0.0, 0.0" // Longitude null
    })
    void testCalculateDistanceInvalidCoordinates(String lat1Str, String lon1Str, String lat2Str, String lon2Str) {
        // Arrange
        BigDecimal lat1 = "null".equals(lat1Str) ? null : new BigDecimal(lat1Str);
        BigDecimal lon1 = "null".equals(lon1Str) ? null : new BigDecimal(lon1Str);
        BigDecimal lat2 = new BigDecimal(lat2Str);
        BigDecimal lon2 = new BigDecimal(lon2Str);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            borneLocalisator.calculateDistance(lat1, lon1, lat2, lon2),
            "Devrait lever une exception pour des coordonnées invalides"
        );
    }



    
    // Tests de la méthode get_nearby_borne
    @Test
    @DisplayName("Test de get_nearby_borne avec des coordonnées valides")
    void testGetNearbyBorne() {
        // Arrange
        when(borneService.findAll()).thenReturn(Collections.singletonList(testBorne));

        // Act
        List<BorneDTO> result = borneLocalisator.get_nearby_borne(testLongitude, testLatitude, testRayon);

        // Assert
        assertNotNull(result, "La liste des bornes ne devrait pas être null");
        assertFalse(result.isEmpty(), "La liste ne devrait pas être vide");
        assertEquals(1, result.size(), "La liste devrait contenir une seule borne");
        assertEquals(entityMapper.toDTO(testBorne), result.get(0), "La borne retournée devrait être la borne de test");
        verify(borneService).findAll();
    }

    @Test
    @DisplayName("Test de get_nearby_borne avec des coordonnées invalides")
    void testGetNearbyBorneWithInvalidCoordinates() {
        // Arrange
        BigDecimal invalidLongitude = new BigDecimal("200.0");
        BigDecimal invalidLatitude = new BigDecimal("100.0");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            borneLocalisator.get_nearby_borne(invalidLongitude, invalidLatitude, testRayon),
            "Devrait lever une exception pour des coordonnées invalides"
        );
        verifyNoInteractions(borneService);
    }




    // Tests de la méthode get_free_borne

    @Test
    @DisplayName("Test de get_free_borne avec un temps où la borne est libre")
    void testGetFreeBorneWhenAvailable() {
        // Arrange
        when(borneService.findAll()).thenReturn(Collections.singletonList(testBorne));
        when(reservationService.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<BorneDTO> result = borneLocalisator.get_free_borne(testTime);

        // Assert
        assertNotNull(result, "La liste des bornes libres ne devrait pas être null");
        assertFalse(result.isEmpty(), "La liste ne devrait pas être vide");
        assertEquals(1, result.size(), "La liste devrait contenir une seule borne");
        assertEquals(entityMapper.toDTO(testBorne), result.get(0), "La borne retournée devrait être la borne de test");
        verify(borneService).findAll();
        verify(reservationService).findAll();
    }

    @Test
    @DisplayName("Test de get_free_borne avec un temps où la borne est occupée")
    void testGetFreeBorneWhenOccupied() {
        // Arrange
        when(borneService.findAll()).thenReturn(Collections.singletonList(testBorne));
        when(reservationService.findAll()).thenReturn(Collections.singletonList(testReservation2));

        // Act
        List<BorneDTO> result = borneLocalisator.get_free_borne(testTime.plusDays(1).plusHours(12));

        // Assert
        assertNotNull(result, "La liste des bornes libres ne devrait pas être null");
        assertTrue(result.isEmpty(), "La liste devrait être vide car la borne est occupée");
        verify(borneService).findAll();
        verify(reservationService).findAll();
    }





    // Tests de la méthode get_free_nearby_borne

    @Test
    @DisplayName("Test de get_free_nearby_borne avec des paramètres valides")
    void testGetFreeNearbyBorne() {
        // Arrange
        when(borneService.findAll()).thenReturn(Collections.singletonList(testBorne));
        when(reservationService.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<BorneDTO> result = borneLocalisator.get_free_nearby_borne(testLongitude, testLatitude, testRayon, testTime);

        // Assert
        assertNotNull(result, "La liste des bornes libres à proximité ne devrait pas être null");
        assertFalse(result.isEmpty(), "La liste ne devrait pas être vide");
        assertEquals(1, result.size(), "La liste devrait contenir une seule borne");
        assertEquals(entityMapper.toDTO(testBorne), result.get(0), "La borne retournée devrait être la borne de test");
        verify(borneService, times(2)).findAll();
        verify(reservationService).findAll();
    }

    @Test
    @DisplayName("Test de get_free_nearby_borne avec des coordonnées invalides")
    void testGetFreeNearbyBorneWithInvalidCoordinates() {
        // Arrange
        BigDecimal invalidLongitude = new BigDecimal("200.0");
        BigDecimal invalidLatitude = new BigDecimal("100.0");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            borneLocalisator.get_free_nearby_borne(invalidLongitude, invalidLatitude, testRayon, testTime),
            "Devrait lever une exception pour des coordonnées invalides"
        );
        verifyNoInteractions(borneService);
        verifyNoInteractions(reservationService);
    }

    @Test
    @DisplayName("Test de get_free_nearby_borne avec un temps null")
    void testGetFreeNearbyBorneWithNullTime() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            borneLocalisator.get_free_nearby_borne(testLongitude, testLatitude, testRayon, null),
            "Devrait lever une exception pour un temps null"
        );
        verifyNoInteractions(borneService);
        verifyNoInteractions(reservationService);
    }
} 