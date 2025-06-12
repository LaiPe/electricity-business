package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.*;
import com.electricitybusiness.api.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Tests unitaires des services")
class ServicesTest {

    @Nested
    @DisplayName("Tests du BorneService")
    class BorneServiceTest {
        @Mock
        private BorneRepository borneRepository;

        private BorneService borneService;
        private Borne testBorne;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            borneService = new BorneService(borneRepository);

            // Création de la borne de test
            testBorne = new Borne();
            testBorne.setNumBorne(1L);
            testBorne.setNomBorne("Borne de test");
            testBorne.setLatitude(new BigDecimal("48.8566"));
            testBorne.setLongitude(new BigDecimal("2.3522"));
            testBorne.setPuissance(new BigDecimal("50.0"));
            testBorne.setInstruction("Instructions de test");
            testBorne.setSurPied(true);
            testBorne.setEtat(EtatBorne.ACTIVE);
            testBorne.setOccupee(false);
        }

        @Test
        @DisplayName("Test de la sauvegarde d'une borne")
        void testSave() {
            // Arrange
            when(borneRepository.save(any(Borne.class))).thenReturn(testBorne);

            // Act
            Borne result = borneService.save(testBorne);

            // Assert
            assertNotNull(result);
            assertEquals(testBorne.getNumBorne(), result.getNumBorne());
            verify(borneRepository).save(testBorne);
        }

        @Test
        @DisplayName("Test de la recherche d'une borne par ID")
        void testFindById() {
            // Arrange
            when(borneRepository.findById(1L)).thenReturn(Optional.of(testBorne));

            // Act
            Optional<Borne> result = borneService.findById(1L);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(testBorne.getNumBorne(), result.get().getNumBorne());
            verify(borneRepository).findById(1L);
        }

        @Test
        @DisplayName("Test de la modification d'une borne")
        void testUpdate() {
            // Arrange
            Borne updatedBorne = new Borne();
            updatedBorne.setNumBorne(1L);
            updatedBorne.setNomBorne("Borne modifiée");
            updatedBorne.setLatitude(new BigDecimal("48.8566"));
            updatedBorne.setLongitude(new BigDecimal("2.3522"));
            updatedBorne.setPuissance(new BigDecimal("75.0"));
            updatedBorne.setInstruction("Nouvelles instructions");
            updatedBorne.setSurPied(true);
            updatedBorne.setEtat(EtatBorne.ACTIVE);
            updatedBorne.setOccupee(false);

            when(borneRepository.save(any(Borne.class))).thenReturn(updatedBorne);

            // Act
            Borne result = borneService.update(1L, updatedBorne);

            // Assert
            assertNotNull(result);
            assertEquals(updatedBorne.getNomBorne(), result.getNomBorne());
            assertEquals(updatedBorne.getPuissance(), result.getPuissance());
            assertEquals(updatedBorne.getInstruction(), result.getInstruction());
            verify(borneRepository).save(any(Borne.class));
        }

        @Test
        @DisplayName("Test de la suppression d'une borne")
        void testDelete() {
            // Arrange
            doNothing().when(borneRepository).deleteById(1L);

            // Act
            borneService.deleteById(1L);

            // Assert
            verify(borneRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("Tests du ReservationService")
    class ReservationServiceTest {
        @Mock
        private ReservationRepository reservationRepository;

        private ReservationService reservationService;
        private Reservation testReservation;
        private Borne testBorne;
        private Utilisateur testUtilisateur;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            reservationService = new ReservationService(reservationRepository);

            // Création des objets de test
            testBorne = new Borne();
            testBorne.setNumBorne(1L);
            testBorne.setEtat(EtatBorne.ACTIVE);

            testUtilisateur = new Utilisateur();
            testUtilisateur.setNumUtilisateur(1L);
            testUtilisateur.setRole(RoleUtilisateur.CLIENT);

            testReservation = new Reservation();
            testReservation.setNumReservation(1L);
            testReservation.setDateDebut(LocalDateTime.now());
            testReservation.setDateFin(LocalDateTime.now().plusHours(2));
            testReservation.setEtat(EtatReservation.ACCEPTEE);
            testReservation.setBorne(testBorne);
            testReservation.setUtilisateur(testUtilisateur);
            testReservation.setPrixMinuteHisto(new BigDecimal("0.50"));
        }

        @Test
        @DisplayName("Test de la création d'une réservation")
        void testSave() {
            // Arrange
            when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

            // Act
            Reservation result = reservationService.save(testReservation);

            // Assert
            assertNotNull(result);
            assertEquals(testReservation.getNumReservation(), result.getNumReservation());
            verify(reservationRepository).save(testReservation);
        }

        @Test
        @DisplayName("Test de la recherche d'une réservation par ID")
        void testFindById() {
            // Arrange
            when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));

            // Act
            Optional<Reservation> result = reservationService.findById(1L);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(testReservation.getNumReservation(), result.get().getNumReservation());
            verify(reservationRepository).findById(1L);
        }

        @Test
        @DisplayName("Test de la modification d'une réservation")
        void testUpdate() {
            // Arrange
            Reservation updatedReservation = new Reservation();
            updatedReservation.setNumReservation(1L);
            updatedReservation.setDateDebut(LocalDateTime.now().plusDays(1));
            updatedReservation.setDateFin(LocalDateTime.now().plusDays(2));
            updatedReservation.setEtat(EtatReservation.ACCEPTEE);
            updatedReservation.setBorne(testBorne);
            updatedReservation.setUtilisateur(testUtilisateur);
            updatedReservation.setPrixMinuteHisto(new BigDecimal("0.75"));

            when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedReservation);

            // Act
            Reservation result = reservationService.update(1L, updatedReservation);

            // Assert
            assertNotNull(result);
            assertEquals(updatedReservation.getDateDebut(), result.getDateDebut());
            assertEquals(updatedReservation.getDateFin(), result.getDateFin());
            assertEquals(updatedReservation.getPrixMinuteHisto(), result.getPrixMinuteHisto());
            verify(reservationRepository).save(any(Reservation.class));
        }

        @Test
        @DisplayName("Test de la suppression d'une réservation")
        void testDelete() {
            // Arrange
            doNothing().when(reservationRepository).deleteById(1L);

            // Act
            reservationService.deleteById(1L);

            // Assert
            verify(reservationRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("Tests du UtilisateurService")
    class UtilisateurServiceTest {
        @Mock
        private UtilisateurRepository utilisateurRepository;

        private UtilisateurService utilisateurService;
        private Utilisateur testUtilisateur;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            utilisateurService = new UtilisateurService(utilisateurRepository);

            // Création de l'utilisateur de test
            testUtilisateur = new Utilisateur();
            testUtilisateur.setNumUtilisateur(1L);
            testUtilisateur.setNomUtilisateur("Test");
            testUtilisateur.setPrenom("User");
            testUtilisateur.setPseudo("testuser");
            testUtilisateur.setMotDePasse("password123");
            testUtilisateur.setRole(RoleUtilisateur.CLIENT);
            testUtilisateur.setAdresseMail("test.user@example.com");
            testUtilisateur.setDateDeNaissance(LocalDate.now().minusYears(25));
            testUtilisateur.setBanni(false);
            testUtilisateur.setCompteValide(true);
        }

        @Test
        @DisplayName("Test de la création d'un utilisateur")
        void testSave() {
            // Arrange
            when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(testUtilisateur);

            // Act
            Utilisateur result = utilisateurService.save(testUtilisateur);

            // Assert
            assertNotNull(result);
            assertEquals(testUtilisateur.getNumUtilisateur(), result.getNumUtilisateur());
            verify(utilisateurRepository).save(testUtilisateur);
        }

        @Test
        @DisplayName("Test de la recherche d'un utilisateur par ID")
        void testFindById() {
            // Arrange
            when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(testUtilisateur));

            // Act
            Optional<Utilisateur> result = utilisateurService.findById(1L);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(testUtilisateur.getNumUtilisateur(), result.get().getNumUtilisateur());
            verify(utilisateurRepository).findById(1L);
        }

        @Test
        @DisplayName("Test de la modification d'un utilisateur")
        void testUpdate() {
            // Arrange
            Utilisateur updatedUtilisateur = new Utilisateur();
            updatedUtilisateur.setNumUtilisateur(1L);
            updatedUtilisateur.setNomUtilisateur("Updated");
            updatedUtilisateur.setPrenom("User");
            updatedUtilisateur.setPseudo("updateduser");
            updatedUtilisateur.setMotDePasse("newpassword123");
            updatedUtilisateur.setRole(RoleUtilisateur.ADMIN);
            updatedUtilisateur.setAdresseMail("updated.user@example.com");
            updatedUtilisateur.setDateDeNaissance(LocalDate.now().minusYears(30));
            updatedUtilisateur.setBanni(false);
            updatedUtilisateur.setCompteValide(true);

            when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(updatedUtilisateur);

            // Act
            Utilisateur result = utilisateurService.update(1L, updatedUtilisateur);

            // Assert
            assertNotNull(result);
            assertEquals(updatedUtilisateur.getNomUtilisateur(), result.getNomUtilisateur());
            assertEquals(updatedUtilisateur.getPseudo(), result.getPseudo());
            assertEquals(updatedUtilisateur.getRole(), result.getRole());
            verify(utilisateurRepository).save(any(Utilisateur.class));
        }

        @Test
        @DisplayName("Test de la suppression d'un utilisateur")
        void testDelete() {
            // Arrange
            doNothing().when(utilisateurRepository).deleteById(1L);

            // Act
            utilisateurService.deleteById(1L);

            // Assert
            verify(utilisateurRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("Tests du LieuService")
    class LieuServiceTest {
        @Mock
        private LieuRepository lieuRepository;

        private LieuService lieuService;
        private Lieu testLieu;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            lieuService = new LieuService(lieuRepository);

            // Création du lieu de test
            testLieu = new Lieu();
            testLieu.setNumLieu(1L);
            testLieu.setInstructions("Lieu de test");
        }

        @Test
        @DisplayName("Test de la création d'un lieu")
        void testSave() {
            // Arrange
            when(lieuRepository.save(any(Lieu.class))).thenReturn(testLieu);

            // Act
            Lieu result = lieuService.save(testLieu);

            // Assert
            assertNotNull(result);
            assertEquals(testLieu.getNumLieu(), result.getNumLieu());
            verify(lieuRepository).save(testLieu);
        }

        @Test
        @DisplayName("Test de la recherche d'un lieu par ID")
        void testFindById() {
            // Arrange
            when(lieuRepository.findById(1L)).thenReturn(Optional.of(testLieu));

            // Act
            Optional<Lieu> result = lieuService.findById(1L);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(testLieu.getNumLieu(), result.get().getNumLieu());
            verify(lieuRepository).findById(1L);
        }

        @Test
        @DisplayName("Test de la modification d'un lieu")
        void testUpdate() {
            // Arrange
            Lieu updatedLieu = new Lieu();
            updatedLieu.setNumLieu(1L);
            updatedLieu.setInstructions("Lieu modifié");

            when(lieuRepository.save(any(Lieu.class))).thenReturn(updatedLieu);

            // Act
            Lieu result = lieuService.update(1L, updatedLieu);

            // Assert
            assertNotNull(result);
            assertEquals(updatedLieu.getInstructions(), result.getInstructions());
            verify(lieuRepository).save(any(Lieu.class));
        }

        @Test
        @DisplayName("Test de la suppression d'un lieu")
        void testDelete() {
            // Arrange
            doNothing().when(lieuRepository).deleteById(1L);

            // Act
            lieuService.deleteById(1L);

            // Assert
            verify(lieuRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("Tests du AdresseService")
    class AdresseServiceTest {
        @Mock
        private AdresseRepository adresseRepository;

        private AdresseService adresseService;
        private Adresse testAdresse;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            adresseService = new AdresseService(adresseRepository);

            // Création de l'adresse de test
            testAdresse = new Adresse();
            testAdresse.setNumAdresse(1L);
            testAdresse.setNumeroEtRue("123 rue de Test");
            testAdresse.setVille("TestVille");
            testAdresse.setCodePostal("75000");
            testAdresse.setPays("France");
        }

        @Test
        @DisplayName("Test de la création d'une adresse")
        void testSave() {
            // Arrange
            when(adresseRepository.save(any(Adresse.class))).thenReturn(testAdresse);

            // Act
            Adresse result = adresseService.save(testAdresse);

            // Assert
            assertNotNull(result);
            assertEquals(testAdresse.getNumAdresse(), result.getNumAdresse());
            verify(adresseRepository).save(testAdresse);
        }

        @Test
        @DisplayName("Test de la recherche d'une adresse par ID")
        void testFindById() {
            // Arrange
            when(adresseRepository.findById(1L)).thenReturn(Optional.of(testAdresse));

            // Act
            Optional<Adresse> result = adresseService.findById(1L);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(testAdresse.getNumAdresse(), result.get().getNumAdresse());
            verify(adresseRepository).findById(1L);
        }

        @Test
        @DisplayName("Test de la modification d'une adresse")
        void testUpdate() {
            // Arrange
            Adresse updatedAdresse = new Adresse();
            updatedAdresse.setNumAdresse(1L);
            updatedAdresse.setNumeroEtRue("456 rue Modifiée");
            updatedAdresse.setVille("NouvelleVille");
            updatedAdresse.setCodePostal("69000");
            updatedAdresse.setPays("France");

            when(adresseRepository.save(any(Adresse.class))).thenReturn(updatedAdresse);

            // Act
            Adresse result = adresseService.update(1L, updatedAdresse);

            // Assert
            assertNotNull(result);
            assertEquals(updatedAdresse.getNumeroEtRue(), result.getNumeroEtRue());
            assertEquals(updatedAdresse.getVille(), result.getVille());
            assertEquals(updatedAdresse.getCodePostal(), result.getCodePostal());
            verify(adresseRepository).save(any(Adresse.class));
        }

        @Test
        @DisplayName("Test de la suppression d'une adresse")
        void testDelete() {
            // Arrange
            doNothing().when(adresseRepository).deleteById(1L);

            // Act
            adresseService.deleteById(1L);

            // Assert
            verify(adresseRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("Tests du MediaService")
    class MediaServiceTest {
        @Mock
        private MediaRepository mediaRepository;

        private MediaService mediaService;
        private Media testMedia;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            mediaService = new MediaService(mediaRepository);

            // Création du média de test
            testMedia = new Media();
            testMedia.setNumMedia(1L);
            testMedia.setType("image");
            testMedia.setUrl("http://example.com/test.jpg");
            testMedia.setDescription("Description du média de test");
            testMedia.setNomMedia("Test Media");
        }

        @Test
        @DisplayName("Test de la création d'un média")
        void testSave() {
            // Arrange
            when(mediaRepository.save(any(Media.class))).thenReturn(testMedia);

            // Act
            Media result = mediaService.save(testMedia);

            // Assert
            assertNotNull(result);
            assertEquals(testMedia.getNumMedia(), result.getNumMedia());
            verify(mediaRepository).save(testMedia);
        }

        @Test
        @DisplayName("Test de la recherche d'un média par ID")
        void testFindById() {
            // Arrange
            when(mediaRepository.findById(1L)).thenReturn(Optional.of(testMedia));

            // Act
            Optional<Media> result = mediaService.findById(1L);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(testMedia.getNumMedia(), result.get().getNumMedia());
            verify(mediaRepository).findById(1L);
        }

        @Test
        @DisplayName("Test de la modification d'un média")
        void testUpdate() {
            // Arrange
            Media updatedMedia = new Media();
            updatedMedia.setNumMedia(1L);
            updatedMedia.setType("video");
            updatedMedia.setUrl("http://example.com/test.mp4");
            updatedMedia.setDescription("Nouvelle description");
            updatedMedia.setNomMedia("Updated Media");

            when(mediaRepository.save(any(Media.class))).thenReturn(updatedMedia);

            // Act
            Media result = mediaService.update(1L, updatedMedia);

            // Assert
            assertNotNull(result);
            assertEquals(updatedMedia.getType(), result.getType());
            assertEquals(updatedMedia.getUrl(), result.getUrl());
            assertEquals(updatedMedia.getDescription(), result.getDescription());
            assertEquals(updatedMedia.getNomMedia(), result.getNomMedia());
            verify(mediaRepository).save(any(Media.class));
        }

        @Test
        @DisplayName("Test de la suppression d'un média")
        void testDelete() {
            // Arrange
            doNothing().when(mediaRepository).deleteById(1L);

            // Act
            mediaService.deleteById(1L);

            // Assert
            verify(mediaRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("Tests du TarifHoraireService")
    class TarifHoraireServiceTest {
        @Mock
        private TarifHoraireRepository tarifHoraireRepository;

        private TarifHoraireService tarifHoraireService;
        private TarifHoraire testTarif;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            tarifHoraireService = new TarifHoraireService(tarifHoraireRepository);

            // Création du tarif de test
            testTarif = new TarifHoraire();
            testTarif.setNumTarif(1L);
            testTarif.setTarifParMinute(new BigDecimal("0.50"));
            testTarif.setHeureDebut(LocalTime.of(8, 0));
            testTarif.setHeureFin(LocalTime.of(18, 0));
            testTarif.setDateDebut(LocalDate.now());
            testTarif.setActif(true);
        }

        @Test
        @DisplayName("Test de la création d'un tarif horaire")
        void testSave() {
            // Arrange
            when(tarifHoraireRepository.save(any(TarifHoraire.class))).thenReturn(testTarif);

            // Act
            TarifHoraire result = tarifHoraireService.save(testTarif);

            // Assert
            assertNotNull(result);
            assertEquals(testTarif.getNumTarif(), result.getNumTarif());
            verify(tarifHoraireRepository).save(testTarif);
        }

        @Test
        @DisplayName("Test de la recherche d'un tarif horaire par ID")
        void testFindById() {
            // Arrange
            when(tarifHoraireRepository.findById(1L)).thenReturn(Optional.of(testTarif));

            // Act
            Optional<TarifHoraire> result = tarifHoraireService.findById(1L);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(testTarif.getNumTarif(), result.get().getNumTarif());
            verify(tarifHoraireRepository).findById(1L);
        }

        @Test
        @DisplayName("Test de la modification d'un tarif horaire")
        void testUpdate() {
            // Arrange
            TarifHoraire updatedTarif = new TarifHoraire();
            updatedTarif.setNumTarif(1L);
            updatedTarif.setTarifParMinute(new BigDecimal("0.75"));
            updatedTarif.setHeureDebut(LocalTime.of(9, 0));
            updatedTarif.setHeureFin(LocalTime.of(19, 0));
            updatedTarif.setDateDebut(LocalDate.now());
            updatedTarif.setActif(true);

            when(tarifHoraireRepository.save(any(TarifHoraire.class))).thenReturn(updatedTarif);

            // Act
            TarifHoraire result = tarifHoraireService.update(1L, updatedTarif);

            // Assert
            assertNotNull(result);
            assertEquals(updatedTarif.getTarifParMinute(), result.getTarifParMinute());
            assertEquals(updatedTarif.getHeureDebut(), result.getHeureDebut());
            assertEquals(updatedTarif.getHeureFin(), result.getHeureFin());
            verify(tarifHoraireRepository).save(any(TarifHoraire.class));
        }

        @Test
        @DisplayName("Test de la suppression d'un tarif horaire")
        void testDelete() {
            // Arrange
            doNothing().when(tarifHoraireRepository).deleteById(1L);

            // Act
            tarifHoraireService.deleteById(1L);

            // Assert
            verify(tarifHoraireRepository).deleteById(1L);
        }
    }
}
