package com.electricitybusiness.api.utils;

import com.electricitybusiness.api.dto.BorneDTO;
import com.electricitybusiness.api.model.*;
import com.electricitybusiness.api.service.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
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