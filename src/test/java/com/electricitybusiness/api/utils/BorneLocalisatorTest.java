package com.electricitybusiness.api.utils;

import com.electricitybusiness.api.dto.BorneDTO;
import com.electricitybusiness.api.model.*;
import com.electricitybusiness.api.service.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BorneLocalisatorTest {

    @Autowired
    private BorneService borneService;

    @Autowired
    private LieuService lieuService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private ReservationService reservationService;

    private static Borne testBorne;
    private static Lieu testLieu;
    private static Utilisateur testUtilisateur;
    private static Reservation testReservation1;
    private static Reservation testReservation2;
    private BigDecimal testLongitude;
    private BigDecimal testLatitude;
    private double testRayon;
    private LocalDateTime testTime;

    @BeforeAll
    static void setUpTestData(@Autowired BorneService borneService, 
                            @Autowired LieuService lieuService,
                            @Autowired UtilisateurService utilisateurService,
                            @Autowired ReservationService reservationService) {
        // Arrange: Création du lieu de test
        Lieu lieu = new Lieu();
        lieu.setInstructions("Lieu de test pour les bornes");
        testLieu = lieuService.save(lieu);

        // Arrange: Création de l'utilisateur de test
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("Test");
        utilisateur.setPrenom("User");
        utilisateur.setPseudo("testuser");
        utilisateur.setMotDePasse("password123");
        utilisateur.setRole(RoleUtilisateur.CLIENT);
        utilisateur.setAdresseMail("test.user@example.com");
        utilisateur.setDateDeNaissance(LocalDate.now().minusYears(25));
        utilisateur.setBanni(false);
        utilisateur.setCompteValide(true);
        utilisateur.setLieu(testLieu);
        testUtilisateur = utilisateurService.save(utilisateur);

        // Arrange: Création de la borne de test
        Borne borne = new Borne();
        borne.setNomBorne("Borne de test");
        borne.setLatitude(new BigDecimal("48.8566")); // Paris latitude
        borne.setLongitude(new BigDecimal("2.3522")); // Paris longitude
        borne.setPuissance(new BigDecimal("50.0"));
        borne.setInstruction("Instructions de test");
        borne.setSurPied(true);
        borne.setEtat(EtatBorne.ACTIVE);
        borne.setOccupee(false);
        borne.setLieu(testLieu);
        borne.setDateCreation(LocalDateTime.now());
        testBorne = borneService.save(borne);

        // Arrange: Création des réservations de test
        // Réservation 1: Passée
        Reservation reservation1 = new Reservation();
        reservation1.setDateDebut(LocalDateTime.now().minusDays(2));
        reservation1.setDateFin(LocalDateTime.now().minusDays(1));
        reservation1.setPrixMinuteHisto(new BigDecimal("0.50"));
        reservation1.setEtat(EtatReservation.TERMINEE);
        reservation1.setDateCreation(LocalDateTime.now().minusDays(2));
        reservation1.setDateValidation(LocalDateTime.now().minusDays(2));
        reservation1.setMontantTotal(new BigDecimal("720.00")); // 24h * 0.50€/min
        reservation1.setRecuGenere(true);
        reservation1.setUtilisateur(testUtilisateur);
        reservation1.setBorne(testBorne);
        testReservation1 = reservationService.save(reservation1);

        // Réservation 2: Future
        Reservation reservation2 = new Reservation();
        reservation2.setDateDebut(LocalDateTime.now().plusDays(1));
        reservation2.setDateFin(LocalDateTime.now().plusDays(2));
        reservation2.setPrixMinuteHisto(new BigDecimal("0.50"));
        reservation2.setEtat(EtatReservation.ACCEPTEE);
        reservation2.setDateCreation(LocalDateTime.now());
        reservation2.setUtilisateur(testUtilisateur);
        reservation2.setBorne(testBorne);
        testReservation2 = reservationService.save(reservation2);
    }

    @AfterAll
    static void tearDown(@Autowired BorneService borneService, 
                        @Autowired LieuService lieuService,
                        @Autowired ReservationService reservationService,
                        @Autowired UtilisateurService utilisateurService) {
        // Cleanup: Suppression des réservations de test
        if (testReservation1 != null) {
            reservationService.deleteById(testReservation1.getNumReservation());
        }
        if (testReservation2 != null) {
            reservationService.deleteById(testReservation2.getNumReservation());
        }
        // Cleanup: Suppression de la borne de test
        if (testBorne != null) {
            borneService.deleteById(testBorne.getNumBorne());
        }
        // Cleanup: Suppression de l'utilisateur de test
        if (testUtilisateur != null) {
            utilisateurService.deleteById(testUtilisateur.getNumUtilisateur());
        }
        // Cleanup: Suppression du lieu de test
        if (testLieu != null) {
            lieuService.deleteById(testLieu.getNumLieu());
        }
    }

    @BeforeEach
    void setUp() {
        // Arrange: Initialisation des données de test
        testLongitude = new BigDecimal("2.3522"); // Paris longitude
        testLatitude = new BigDecimal("48.8566"); // Paris latitude
        testRayon = 5.0; // 5 kilomètres
        testTime = LocalDateTime.now();
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
        double distance = BorneLocalisator.calculateDistance(lat1, lon1, lat2, lon2);

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
            BorneLocalisator.calculateDistance(lat1, lon1, lat2, lon2),
            "Devrait lever une exception pour des coordonnées invalides"
        );
    }

    // Tests de la méthode get_nearby_borne

    @Test
    @DisplayName("Test de get_nearby_borne avec des coordonnées valides")
    void testGetNearbyBorne() {
        // Arrange
        // Act
        List<BorneDTO> result = BorneLocalisator.get_nearby_borne(testLongitude, testLatitude, testRayon);

        // Assert
        assertNotNull(result, "La liste des bornes ne devrait pas être null");
        assertFalse(result.isEmpty(), "La liste ne devrait pas être vide");
        assertTrue(result.stream()
                .map(BorneDTO::getNumBorne)
                .collect(Collectors.toList())
                .contains(testBorne.getNumBorne()),
            "La liste devrait contenir la borne de test");
    }

    @Test
    @DisplayName("Test de get_nearby_borne avec des coordonnées invalides")
    void testGetNearbyBorneWithInvalidCoordinates() {
        // Arrange
        BigDecimal invalidLongitude = new BigDecimal("200.0"); // Longitude invalide
        BigDecimal invalidLatitude = new BigDecimal("100.0"); // Latitude invalide

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            BorneLocalisator.get_nearby_borne(invalidLongitude, invalidLatitude, testRayon),
            "Devrait lever une exception pour des coordonnées invalides"
        );
    }











    // Tests de la méthode get_free_borne

    @Test
    @DisplayName("Test de get_free_borne avec un temps où la borne est libre")
    void testGetFreeBorneWhenAvailable() {
        // Arrange: On teste un temps entre les deux réservations (maintenant)
        LocalDateTime testTime = LocalDateTime.now();

        // Act
        List<BorneDTO> result = BorneLocalisator.get_free_borne(testTime);

        // Assert
        assertNotNull(result, "La liste des bornes libres ne devrait pas être null");
        assertFalse(result.isEmpty(), "La liste ne devrait pas être vide");
        assertTrue(result.stream()
                .map(BorneDTO::getNumBorne)
                .collect(Collectors.toList())
                .contains(testBorne.getNumBorne()),
            "La borne de test devrait être libre à ce moment");
    }

    @Test
    @DisplayName("Test de get_free_borne avec un temps où la borne est occupée par une réservation future")
    void testGetFreeBorneWhenOccupied() {
        // Arrange: On teste un temps pendant la réservation future
        LocalDateTime testTime = LocalDateTime.now().plusDays(1).plusHours(12); // Milieu de la réservation future

        // Act
        List<BorneDTO> result = BorneLocalisator.get_free_borne(testTime);

        // Assert
        assertNotNull(result, "La liste des bornes libres ne devrait pas être null");
        assertTrue(result.stream()
                .map(BorneDTO::getNumBorne)
                .collect(Collectors.toList())
                .contains(testBorne.getNumBorne()) == false,
            "La borne de test ne devrait pas être libre pendant une réservation");
    }

    @Test
    @DisplayName("Test de get_free_borne avec un temps où la réservation est terminée")
    void testGetFreeBorneWithPastReservation() {
        // Arrange: On teste un temps après la réservation passée
        LocalDateTime testTime = LocalDateTime.now().minusDays(1).minusHours(1); // Juste après la fin de la réservation passée

        // Act
        List<BorneDTO> result = BorneLocalisator.get_free_borne(testTime);

        // Assert
        assertNotNull(result, "La liste des bornes libres ne devrait pas être null");
        assertFalse(result.isEmpty(), "La liste ne devrait pas être vide");
        assertTrue(result.stream()
                .map(BorneDTO::getNumBorne)
                .collect(Collectors.toList())
                .contains(testBorne.getNumBorne()),
            "La borne de test devrait être libre après une réservation terminée");
    }

    @Test
    @DisplayName("Test de get_free_borne avec un temps null")
    void testGetFreeBorneWithNullTime() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            BorneLocalisator.get_free_borne(null),
            "Devrait lever une exception pour un temps null"
        );
    }

    @Test
    @DisplayName("Test de get_free_borne avec un temps dans le futur lointain")
    void testGetFreeBorneWithFarFutureTime() {
        // Arrange: On teste un temps bien après toutes les réservations
        LocalDateTime testTime = LocalDateTime.now().plusYears(1);

        // Act
        List<BorneDTO> result = BorneLocalisator.get_free_borne(testTime);

        // Assert
        assertNotNull(result, "La liste des bornes libres ne devrait pas être null");
        assertFalse(result.isEmpty(), "La liste ne devrait pas être vide");
        assertTrue(result.stream()
                .map(BorneDTO::getNumBorne)
                .collect(Collectors.toList())
                .contains(testBorne.getNumBorne()),
            "La borne de test devrait être libre dans le futur lointain");
    }




    


    // Tests de la méthode get_free_nearby_borne

    @Test
    @DisplayName("Test de get_free_nearby_borne avec des paramètres valides")
    void testGetFreeNearbyBorne() {
        // Arrange
        // Act
        List<BorneDTO> result = BorneLocalisator.get_free_nearby_borne(testLongitude, testLatitude, testRayon, testTime);

        // Assert
        assertNotNull(result, "La liste des bornes libres à proximité ne devrait pas être null");
        assertFalse(result.isEmpty(), "La liste ne devrait pas être vide");
        assertTrue(result.stream()
                .map(BorneDTO::getNumBorne)
                .collect(Collectors.toList())
                .contains(testBorne.getNumBorne()),
            "La liste devrait contenir la borne de test");
    }

    @Test
    @DisplayName("Test de get_free_nearby_borne avec des coordonnées invalides")
    void testGetFreeNearbyBorneWithInvalidCoordinates() {
        // Arrange
        BigDecimal invalidLongitude = new BigDecimal("200.0");
        BigDecimal invalidLatitude = new BigDecimal("100.0");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            BorneLocalisator.get_free_nearby_borne(invalidLongitude, invalidLatitude, testRayon, testTime),
            "Devrait lever une exception pour des coordonnées invalides"
        );
    }

    @Test
    @DisplayName("Test de get_free_nearby_borne avec un temps null")
    void testGetFreeNearbyBorneWithNullTime() {
        // Arrange
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            BorneLocalisator.get_free_nearby_borne(testLongitude, testLatitude, testRayon, null),
            "Devrait lever une exception pour un temps null"
        );
    }
} 