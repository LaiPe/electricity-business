package com.electricitybusiness.api.mapper;

import com.electricitybusiness.api.dto.*;
import com.electricitybusiness.api.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests du EntityMapper")
class EntityMapperTest {

    private EntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new EntityMapper();
    }

    @Nested
    @DisplayName("Tests de conversion Lieu")
    class LieuMapperTest {
        @Test
        @DisplayName("Test de conversion Lieu vers DTO")
        void testToDTO() {
            // Arrange
            Lieu lieu = new Lieu();
            lieu.setNumLieu(1L);
            lieu.setInstructions("Instructions de test");

            // Act
            LieuDTO dto = mapper.toDTO(lieu);

            // Assert
            assertNotNull(dto);
            assertEquals(lieu.getNumLieu(), dto.getNumLieu());
            assertEquals(lieu.getInstructions(), dto.getInstructions());
        }

        @Test
        @DisplayName("Test de conversion DTO vers Lieu")
        void testToEntity() {
            // Arrange
            LieuDTO dto = new LieuDTO(1L, "Instructions de test");

            // Act
            Lieu lieu = mapper.toEntity(dto);

            // Assert
            assertNotNull(lieu);
            assertEquals(dto.getNumLieu(), lieu.getNumLieu());
            assertEquals(dto.getInstructions(), lieu.getInstructions());
        }
    }

    @Nested
    @DisplayName("Tests de conversion Adresse")
    class AdresseMapperTest {
        @Test
        @DisplayName("Test de conversion Adresse vers DTO")
        void testToDTO() {
            // Arrange
            Adresse adresse = new Adresse();
            adresse.setNumAdresse(1L);
            adresse.setNomAdresse("Adresse Test");
            adresse.setNumeroEtRue("123 rue Test");
            adresse.setCodePostal("75000");
            adresse.setVille("Paris");
            adresse.setPays("France");
            adresse.setRegion("Île-de-France");
            adresse.setComplement("Apt 4B");
            adresse.setEtage("2ème");

            Lieu lieu = new Lieu();
            lieu.setNumLieu(1L);
            lieu.setInstructions("Instructions de test");
            adresse.setLieu(lieu);

            // Act
            AdresseDTO dto = mapper.toDTO(adresse);

            // Assert
            assertNotNull(dto);
            assertEquals(adresse.getNumAdresse(), dto.getNumAdresse());
            assertEquals(adresse.getNomAdresse(), dto.getNomAdresse());
            assertEquals(adresse.getNumeroEtRue(), dto.getNumeroEtRue());
            assertEquals(adresse.getCodePostal(), dto.getCodePostal());
            assertEquals(adresse.getVille(), dto.getVille());
            assertEquals(adresse.getPays(), dto.getPays());
            assertEquals(adresse.getRegion(), dto.getRegion());
            assertEquals(adresse.getComplement(), dto.getComplement());
            assertEquals(adresse.getEtage(), dto.getEtage());
            assertNotNull(dto.getLieu());
            assertEquals(adresse.getLieu().getNumLieu(), dto.getLieu().getNumLieu());
        }

        @Test
        @DisplayName("Test de conversion DTO vers Adresse")
        void testToEntity() {
            // Arrange
            LieuDTO lieuDTO = new LieuDTO(1L, "Instructions de test");
            AdresseDTO dto = new AdresseDTO(
                1L, "Adresse Test", "123 rue Test", "75000", "Paris", "France",
                "Île-de-France", "Apt 4B", "2ème", lieuDTO
            );

            // Act
            Adresse adresse = mapper.toEntity(dto);

            // Assert
            assertNotNull(adresse);
            assertEquals(dto.getNumAdresse(), adresse.getNumAdresse());
            assertEquals(dto.getNomAdresse(), adresse.getNomAdresse());
            assertEquals(dto.getNumeroEtRue(), adresse.getNumeroEtRue());
            assertEquals(dto.getCodePostal(), adresse.getCodePostal());
            assertEquals(dto.getVille(), adresse.getVille());
            assertEquals(dto.getPays(), adresse.getPays());
            assertEquals(dto.getRegion(), adresse.getRegion());
            assertEquals(dto.getComplement(), adresse.getComplement());
            assertEquals(dto.getEtage(), adresse.getEtage());
            assertNotNull(adresse.getLieu());
            assertEquals(dto.getLieu().getNumLieu(), adresse.getLieu().getNumLieu());
        }
    }

    @Nested
    @DisplayName("Tests de conversion Borne")
    class BorneMapperTest {
        @Test
        @DisplayName("Test de conversion Borne vers DTO")
        void testToDTO() {
            // Arrange
            Borne borne = new Borne();
            borne.setNumBorne(1L);
            borne.setNomBorne("Borne Test");
            borne.setLatitude(new BigDecimal("48.8566"));
            borne.setLongitude(new BigDecimal("2.3522"));
            borne.setPuissance(new BigDecimal("50.0"));
            borne.setInstruction("Instructions de test");
            borne.setSurPied(true);
            borne.setEtat(EtatBorne.ACTIVE);
            borne.setOccupee(false);
            borne.setDateCreation(LocalDateTime.now());
            borne.setDerniereMaintenance(LocalDateTime.now());

            Lieu lieu = new Lieu();
            lieu.setNumLieu(1L);
            lieu.setInstructions("Instructions de test");
            borne.setLieu(lieu);

            // Act
            BorneDTO dto = mapper.toDTO(borne);

            // Assert
            assertNotNull(dto);
            assertEquals(borne.getNumBorne(), dto.getNumBorne());
            assertEquals(borne.getNomBorne(), dto.getNomBorne());
            assertEquals(borne.getLatitude(), dto.getLatitude());
            assertEquals(borne.getLongitude(), dto.getLongitude());
            assertEquals(borne.getPuissance(), dto.getPuissance());
            assertEquals(borne.getInstruction(), dto.getInstruction());
            assertEquals(borne.getSurPied(), dto.getSurPied());
            assertEquals(borne.getEtat(), dto.getEtat());
            assertEquals(borne.getOccupee(), dto.getOccupee());
            assertEquals(borne.getDateCreation(), dto.getDateCreation());
            assertEquals(borne.getDerniereMaintenance(), dto.getDerniereMaintenance());
            assertNotNull(dto.getLieu());
            assertEquals(borne.getLieu().getNumLieu(), dto.getLieu().getNumLieu());
        }

        @Test
        @DisplayName("Test de conversion DTO vers Borne")
        void testToEntity() {
            // Arrange
            LieuDTO lieuDTO = new LieuDTO(1L, "Instructions de test");
            BorneDTO dto = new BorneDTO(
                1L, "Borne Test", new BigDecimal("48.8566"), new BigDecimal("2.3522"),
                new BigDecimal("50.0"), "Instructions de test", true, EtatBorne.ACTIVE,
                false, LocalDateTime.now(), LocalDateTime.now(), lieuDTO
            );

            // Act
            Borne borne = mapper.toEntity(dto);

            // Assert
            assertNotNull(borne);
            assertEquals(dto.getNumBorne(), borne.getNumBorne());
            assertEquals(dto.getNomBorne(), borne.getNomBorne());
            assertEquals(dto.getLatitude(), borne.getLatitude());
            assertEquals(dto.getLongitude(), borne.getLongitude());
            assertEquals(dto.getPuissance(), borne.getPuissance());
            assertEquals(dto.getInstruction(), borne.getInstruction());
            assertEquals(dto.getSurPied(), borne.getSurPied());
            assertEquals(dto.getEtat(), borne.getEtat());
            assertEquals(dto.getOccupee(), borne.getOccupee());
            assertEquals(dto.getDateCreation(), borne.getDateCreation());
            assertEquals(dto.getDerniereMaintenance(), borne.getDerniereMaintenance());
            assertNotNull(borne.getLieu());
            assertEquals(dto.getLieu().getNumLieu(), borne.getLieu().getNumLieu());
        }
    }

    @Nested
    @DisplayName("Tests de conversion Media")
    class MediaMapperTest {
        @Test
        @DisplayName("Test de conversion Media vers DTO")
        void testToDTO() {
            // Arrange
            Media media = new Media();
            media.setNumMedia(1L);
            media.setNomMedia("Media Test");
            media.setType("image");
            media.setUrl("http://example.com/test.jpg");
            media.setDescription("Description de test");

            // Act
            MediaDTO dto = mapper.toDTO(media);

            // Assert
            assertNotNull(dto);
            assertEquals(media.getNumMedia(), dto.getNumMedia());
            assertEquals(media.getNomMedia(), dto.getNomMedia());
            assertEquals(media.getType(), dto.getTypeMedia());
            assertEquals(media.getUrl(), dto.getUrlMedia());
            assertEquals(media.getDescription(), dto.getDescription());
        }

        @Test
        @DisplayName("Test de conversion DTO vers Media")
        void testToEntity() {
            // Arrange
            MediaDTO dto = new MediaDTO(
                1L, "Media Test", "image", "http://example.com/test.jpg",
                "Description de test", null
            );

            // Act
            Media media = mapper.toEntity(dto);

            // Assert
            assertNotNull(media);
            assertEquals(dto.getNumMedia(), media.getNumMedia());
            assertEquals(dto.getNomMedia(), media.getNomMedia());
            assertEquals(dto.getTypeMedia(), media.getType());
            assertEquals(dto.getUrlMedia(), media.getUrl());
            assertEquals(dto.getDescription(), media.getDescription());
        }
    }

    @Nested
    @DisplayName("Tests de conversion Reservation")
    class ReservationMapperTest {
        @Test
        @DisplayName("Test de conversion Reservation vers DTO")
        void testToDTO() {
            // Arrange
            Reservation reservation = new Reservation();
            reservation.setNumReservation(1L);
            reservation.setDateDebut(LocalDateTime.now());
            reservation.setDateFin(LocalDateTime.now().plusHours(2));
            reservation.setEtat(EtatReservation.ACCEPTEE);
            reservation.setMontantTotal(new BigDecimal("50.0"));
            reservation.setDateValidation(LocalDateTime.now());

            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNumUtilisateur(1L);
            reservation.setUtilisateur(utilisateur);

            Borne borne = new Borne();
            borne.setNumBorne(1L);
            reservation.setBorne(borne);

            // Act
            ReservationDTO dto = mapper.toDTO(reservation);

            // Assert
            assertNotNull(dto);
            assertEquals(reservation.getNumReservation(), dto.getId());
            assertEquals(reservation.getDateDebut(), dto.getDateDebutReservation());
            assertEquals(reservation.getDateFin(), dto.getDateFinReservation());
            assertEquals(reservation.getEtat(), dto.getEtatReservation());
            assertEquals(reservation.getMontantTotal(), dto.getMontantPaye());
            assertEquals(reservation.getDateValidation(), dto.getDatePaiement());
            assertEquals(reservation.getUtilisateur().getNumUtilisateur(), dto.getNumUtilisateur());
            assertEquals(reservation.getBorne().getNumBorne(), dto.getNumBorne());
        }

        @Test
        @DisplayName("Test de conversion DTO vers Reservation")
        void testToEntity() {
            // Arrange
            ReservationDTO dto = new ReservationDTO(
                1L, LocalDateTime.now(), LocalDateTime.now().plusHours(2),
                EtatReservation.ACCEPTEE, new BigDecimal("50.0"),
                LocalDateTime.now(), 1L, 1L
            );

            // Act
            Reservation reservation = mapper.toEntity(dto);

            // Assert
            assertNotNull(reservation);
            assertEquals(dto.getId(), reservation.getNumReservation());
            assertEquals(dto.getDateDebutReservation(), reservation.getDateDebut());
            assertEquals(dto.getDateFinReservation(), reservation.getDateFin());
            assertEquals(dto.getEtatReservation(), reservation.getEtat());
            assertEquals(dto.getMontantPaye(), reservation.getMontantTotal());
            assertEquals(dto.getDatePaiement(), reservation.getDateValidation());
            // Note: Les références Utilisateur et Borne doivent être résolues par le service
            assertNull(reservation.getUtilisateur());
            assertNull(reservation.getBorne());
        }
    }

    @Nested
    @DisplayName("Tests de conversion TarifHoraire")
    class TarifHoraireMapperTest {
        @Test
        @DisplayName("Test de conversion TarifHoraire vers DTO")
        void testToDTO() {
            // Arrange
            TarifHoraire tarif = new TarifHoraire();
            tarif.setNumTarif(1L);
            tarif.setTarifParMinute(new BigDecimal("0.50"));
            tarif.setHeureDebut(LocalTime.of(8, 0));
            tarif.setHeureFin(LocalTime.of(18, 0));
            tarif.setJourSemaine(1);
            tarif.setDateDebut(LocalDate.now());
            tarif.setDateFin(LocalDate.now().plusMonths(1));
            tarif.setActif(true);

            Borne borne = new Borne();
            borne.setNumBorne(1L);
            tarif.setBorne(borne);

            // Act
            TarifHoraireDTO dto = mapper.toDTO(tarif);

            // Assert
            assertNotNull(dto);
            assertEquals(tarif.getNumTarif(), dto.getNumTarif());
            assertEquals(tarif.getTarifParMinute(), dto.getTarifParMinute());
            assertEquals(tarif.getHeureDebut(), dto.getHeureDebut());
            assertEquals(tarif.getHeureFin(), dto.getHeureFin());
            assertEquals(tarif.getJourSemaine(), dto.getJourSemaine());
            assertEquals(tarif.getDateDebut(), dto.getDateDebut());
            assertEquals(tarif.getDateFin(), dto.getDateFin());
            assertEquals(tarif.getActif(), dto.getActif());
            assertEquals(tarif.getBorne().getNumBorne(), dto.getNumBorne());
        }

        @Test
        @DisplayName("Test de conversion DTO vers TarifHoraire")
        void testToEntity() {
            // Arrange
            TarifHoraireDTO dto = new TarifHoraireDTO(
                1L, new BigDecimal("0.50"), LocalTime.of(8, 0), LocalTime.of(18, 0),
                1, LocalDate.now(), LocalDate.now().plusMonths(1), true, 1L
            );

            // Act
            TarifHoraire tarif = mapper.toEntity(dto);

            // Assert
            assertNotNull(tarif);
            assertEquals(dto.getNumTarif(), tarif.getNumTarif());
            assertEquals(dto.getTarifParMinute(), tarif.getTarifParMinute());
            assertEquals(dto.getHeureDebut(), tarif.getHeureDebut());
            assertEquals(dto.getHeureFin(), tarif.getHeureFin());
            assertEquals(dto.getJourSemaine(), tarif.getJourSemaine());
            assertEquals(dto.getDateDebut(), tarif.getDateDebut());
            assertEquals(dto.getDateFin(), tarif.getDateFin());
            assertEquals(dto.getActif(), tarif.getActif());
            // Note: La référence Borne doit être résolue par le service
            assertNull(tarif.getBorne());
        }
    }
} 