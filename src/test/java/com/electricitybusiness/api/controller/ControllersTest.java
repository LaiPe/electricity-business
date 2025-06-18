package com.electricitybusiness.api.controller;

import com.electricitybusiness.api.dto.*;
import com.electricitybusiness.api.mapper.EntityMapper;
import com.electricitybusiness.api.model.*;
import com.electricitybusiness.api.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({
    AdresseController.class,
    BorneController.class,
    LieuController.class,
    MediaController.class,
    ReservationController.class,
    TarifHoraireController.class,
    UtilisateurController.class
})
class ControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdresseService adresseService;
    @MockBean
    private BorneService borneService;
    @MockBean
    private LieuService lieuService;
    @MockBean
    private MediaService mediaService;
    @MockBean
    private ReservationService reservationService;
    @MockBean
    private TarifHoraireService tarifHoraireService;
    @MockBean
    private UtilisateurService utilisateurService;
    @MockBean
    private EntityMapper entityMapper;

    @Nested
    @DisplayName("Tests du AdresseController")
    class AdresseControllerTest {
        private Adresse testAdresse;
        private AdresseDTO testAdresseDTO;

        @BeforeEach
        void setUp() {
            testAdresse = new Adresse();
            testAdresse.setNumAdresse(1L);
            testAdresse.setNomAdresse("Test Adresse");
            testAdresse.setNumeroEtRue("123 rue Test");
            testAdresse.setCodePostal("75000");
            testAdresse.setVille("Paris");
            testAdresse.setPays("France");

            testAdresseDTO = new AdresseDTO(
                1L, "Test Adresse", "123 rue Test", "75000", "Paris", "France",
                "Île-de-France", "Apt 4B", "2ème", null
            );

            when(entityMapper.toDTO(any(Adresse.class))).thenReturn(testAdresseDTO);
            when(entityMapper.toEntity(any(AdresseDTO.class))).thenReturn(testAdresse);
        }

        @Test
        @DisplayName("GET /api/adresses - Devrait retourner toutes les adresses")
        void testGetAllAdresses() throws Exception {
            when(adresseService.findAll()).thenReturn(Arrays.asList(testAdresse));

            mockMvc.perform(get("/api/adresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numAdresse").value(1))
                .andExpect(jsonPath("$[0].nomAdresse").value("Test Adresse"));
        }

        @Test
        @DisplayName("GET /api/adresses/{id} - Devrait retourner une adresse par ID")
        void testGetAdresseById() throws Exception {
            when(adresseService.findById(1L)).thenReturn(Optional.of(testAdresse));

            mockMvc.perform(get("/api/adresses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numAdresse").value(1))
                .andExpect(jsonPath("$.nomAdresse").value("Test Adresse"));
        }

        @Test
        @DisplayName("POST /api/adresses - Devrait créer une nouvelle adresse")
        void testCreateAdresse() throws Exception {
            when(adresseService.save(any(Adresse.class))).thenReturn(testAdresse);

            mockMvc.perform(post("/api/adresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAdresseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numAdresse").value(1));
        }

        @Test
        @DisplayName("PUT /api/adresses/{id} - Devrait mettre à jour une adresse")
        void testUpdateAdresse() throws Exception {
            when(adresseService.existsById(1L)).thenReturn(true);
            when(adresseService.update(anyLong(), any(Adresse.class))).thenReturn(testAdresse);

            mockMvc.perform(put("/api/adresses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAdresseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numAdresse").value(1));
        }

        @Test
        @DisplayName("DELETE /api/adresses/{id} - Devrait supprimer une adresse")
        void testDeleteAdresse() throws Exception {
            when(adresseService.existsById(1L)).thenReturn(true);
            doNothing().when(adresseService).deleteById(1L);

            mockMvc.perform(delete("/api/adresses/1"))
                .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Tests du BorneController")
    class BorneControllerTest {
        private Borne testBorne;

        @BeforeEach
        void setUp() {
            testBorne = new Borne();
            testBorne.setNumBorne(1L);
            testBorne.setNomBorne("Test Borne");
            testBorne.setLatitude(new BigDecimal("48.8566"));
            testBorne.setLongitude(new BigDecimal("2.3522"));
            testBorne.setPuissance(new BigDecimal("50.0"));
        }

        @Test
        @DisplayName("GET /api/bornes - Devrait retourner toutes les bornes")
        void testGetAllBornes() throws Exception {
            when(borneService.findAll()).thenReturn(Arrays.asList(testBorne));

            mockMvc.perform(get("/api/bornes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numBorne").value(1))
                .andExpect(jsonPath("$[0].nomBorne").value("Test Borne"));
        }

        @Test
        @DisplayName("GET /api/bornes/{id} - Devrait retourner une borne par ID")
        void testGetBorneById() throws Exception {
            when(borneService.findById(1L)).thenReturn(Optional.of(testBorne));

            mockMvc.perform(get("/api/bornes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numBorne").value(1))
                .andExpect(jsonPath("$.nomBorne").value("Test Borne"));
        }

        @Test
        @DisplayName("POST /api/bornes - Devrait créer une nouvelle borne")
        void testCreateBorne() throws Exception {
            when(borneService.save(any(Borne.class))).thenReturn(testBorne);

            mockMvc.perform(post("/api/bornes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBorne)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numBorne").value(1));
        }

        @Test
        @DisplayName("PUT /api/bornes/{id} - Devrait mettre à jour une borne")
        void testUpdateBorne() throws Exception {
            when(borneService.existsById(1L)).thenReturn(true);
            when(borneService.update(anyLong(), any(Borne.class))).thenReturn(testBorne);

            mockMvc.perform(put("/api/bornes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBorne)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numBorne").value(1));
        }

        @Test
        @DisplayName("DELETE /api/bornes/{id} - Devrait supprimer une borne")
        void testDeleteBorne() throws Exception {
            when(borneService.existsById(1L)).thenReturn(true);
            doNothing().when(borneService).deleteById(1L);

            mockMvc.perform(delete("/api/bornes/1"))
                .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Tests du LieuController")
    class LieuControllerTest {
        private Lieu testLieu;
        private LieuDTO testLieuDTO;

        @BeforeEach
        void setUp() {
            testLieu = new Lieu();
            testLieu.setNumLieu(1L);
            testLieu.setInstructions("Test Instructions");

            testLieuDTO = new LieuDTO(1L, "Test Instructions");

            when(entityMapper.toDTO(any(Lieu.class))).thenReturn(testLieuDTO);
            when(entityMapper.toEntity(any(LieuDTO.class))).thenReturn(testLieu);
        }

        @Test
        @DisplayName("GET /api/lieux - Devrait retourner tous les lieux")
        void testGetAllLieux() throws Exception {
            when(lieuService.findAll()).thenReturn(Arrays.asList(testLieu));

            mockMvc.perform(get("/api/lieux"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numLieu").value(1))
                .andExpect(jsonPath("$[0].instructions").value("Test Instructions"));
        }

        @Test
        @DisplayName("GET /api/lieux/{id} - Devrait retourner un lieu par ID")
        void testGetLieuById() throws Exception {
            when(lieuService.findById(1L)).thenReturn(Optional.of(testLieu));

            mockMvc.perform(get("/api/lieux/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numLieu").value(1))
                .andExpect(jsonPath("$.instructions").value("Test Instructions"));
        }

        @Test
        @DisplayName("POST /api/lieux - Devrait créer un nouveau lieu")
        void testCreateLieu() throws Exception {
            when(lieuService.save(any(Lieu.class))).thenReturn(testLieu);

            mockMvc.perform(post("/api/lieux")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLieuDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numLieu").value(1));
        }

        @Test
        @DisplayName("PUT /api/lieux/{id} - Devrait mettre à jour un lieu")
        void testUpdateLieu() throws Exception {
            when(lieuService.existsById(1L)).thenReturn(true);
            when(lieuService.update(anyLong(), any(Lieu.class))).thenReturn(testLieu);

            mockMvc.perform(put("/api/lieux/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLieuDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numLieu").value(1));
        }

        @Test
        @DisplayName("DELETE /api/lieux/{id} - Devrait supprimer un lieu")
        void testDeleteLieu() throws Exception {
            when(lieuService.existsById(1L)).thenReturn(true);
            doNothing().when(lieuService).deleteById(1L);

            mockMvc.perform(delete("/api/lieux/1"))
                .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Tests du MediaController")
    class MediaControllerTest {
        private Media testMedia;

        @BeforeEach
        void setUp() {
            testMedia = new Media();
            testMedia.setNumMedia(1L);
            testMedia.setNomMedia("Test Media");
            testMedia.setType("image");
            testMedia.setUrl("http://example.com/test.jpg");
            testMedia.setDescription("Test Description");
        }

        @Test
        @DisplayName("GET /api/medias - Devrait retourner tous les médias")
        void testGetAllMedias() throws Exception {
            when(mediaService.findAll()).thenReturn(Arrays.asList(testMedia));

            mockMvc.perform(get("/api/medias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numMedia").value(1))
                .andExpect(jsonPath("$[0].nomMedia").value("Test Media"));
        }

        @Test
        @DisplayName("GET /api/medias/{id} - Devrait retourner un média par ID")
        void testGetMediaById() throws Exception {
            when(mediaService.findById(1L)).thenReturn(Optional.of(testMedia));

            mockMvc.perform(get("/api/medias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numMedia").value(1))
                .andExpect(jsonPath("$.nomMedia").value("Test Media"));
        }

        @Test
        @DisplayName("POST /api/medias - Devrait créer un nouveau média")
        void testCreateMedia() throws Exception {
            when(mediaService.save(any(Media.class))).thenReturn(testMedia);

            mockMvc.perform(post("/api/medias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMedia)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numMedia").value(1));
        }

        @Test
        @DisplayName("PUT /api/medias/{id} - Devrait mettre à jour un média")
        void testUpdateMedia() throws Exception {
            when(mediaService.existsById(1L)).thenReturn(true);
            when(mediaService.update(anyLong(), any(Media.class))).thenReturn(testMedia);

            mockMvc.perform(put("/api/medias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMedia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numMedia").value(1));
        }

        @Test
        @DisplayName("DELETE /api/medias/{id} - Devrait supprimer un média")
        void testDeleteMedia() throws Exception {
            when(mediaService.existsById(1L)).thenReturn(true);
            doNothing().when(mediaService).deleteById(1L);

            mockMvc.perform(delete("/api/medias/1"))
                .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Tests du ReservationController")
    class ReservationControllerTest {
        private Reservation testReservation;

        @BeforeEach
        void setUp() {
            testReservation = new Reservation();
            testReservation.setNumReservation(1L);
            testReservation.setDateDebut(LocalDateTime.now());
            testReservation.setDateFin(LocalDateTime.now().plusHours(2));
            testReservation.setEtat(EtatReservation.ACCEPTEE);
            testReservation.setPrixMinuteHisto(new BigDecimal("0.50"));
        }

        @Test
        @DisplayName("GET /api/reservations - Devrait retourner toutes les réservations")
        void testGetAllReservations() throws Exception {
            when(reservationService.findAll()).thenReturn(Arrays.asList(testReservation));

            mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numReservation").value(1));
        }

        @Test
        @DisplayName("GET /api/reservations/{id} - Devrait retourner une réservation par ID")
        void testGetReservationById() throws Exception {
            when(reservationService.findById(1L)).thenReturn(Optional.of(testReservation));

            mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numReservation").value(1));
        }

        @Test
        @DisplayName("POST /api/reservations - Devrait créer une nouvelle réservation")
        void testCreateReservation() throws Exception {
            when(reservationService.save(any(Reservation.class))).thenReturn(testReservation);

            mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testReservation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numReservation").value(1));
        }

        @Test
        @DisplayName("PUT /api/reservations/{id} - Devrait mettre à jour une réservation")
        void testUpdateReservation() throws Exception {
            when(reservationService.existsById(1L)).thenReturn(true);
            when(reservationService.update(anyLong(), any(Reservation.class))).thenReturn(testReservation);

            mockMvc.perform(put("/api/reservations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testReservation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numReservation").value(1));
        }

        @Test
        @DisplayName("DELETE /api/reservations/{id} - Devrait supprimer une réservation")
        void testDeleteReservation() throws Exception {
            when(reservationService.existsById(1L)).thenReturn(true);
            doNothing().when(reservationService).deleteById(1L);

            mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Tests du TarifHoraireController")
    class TarifHoraireControllerTest {
        private TarifHoraire testTarif;

        @BeforeEach
        void setUp() {
            testTarif = new TarifHoraire();
            testTarif.setNumTarif(1L);
            testTarif.setTarifParMinute(new BigDecimal("0.50"));
            testTarif.setHeureDebut(LocalTime.of(8, 0));
            testTarif.setHeureFin(LocalTime.of(18, 0));
            testTarif.setJourSemaine(1);
            testTarif.setDateDebut(LocalDate.now());
            testTarif.setDateFin(LocalDate.now().plusMonths(1));
            testTarif.setActif(true);
        }

        @Test
        @DisplayName("GET /api/tarifs-horaires - Devrait retourner tous les tarifs horaires")
        void testGetAllTarifsHoraires() throws Exception {
            when(tarifHoraireService.findAll()).thenReturn(Arrays.asList(testTarif));

            mockMvc.perform(get("/api/tarifs-horaires"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numTarif").value(1));
        }

        @Test
        @DisplayName("GET /api/tarifs-horaires/{id} - Devrait retourner un tarif horaire par ID")
        void testGetTarifHoraireById() throws Exception {
            when(tarifHoraireService.findById(1L)).thenReturn(Optional.of(testTarif));

            mockMvc.perform(get("/api/tarifs-horaires/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numTarif").value(1));
        }

        @Test
        @DisplayName("POST /api/tarifs-horaires - Devrait créer un nouveau tarif horaire")
        void testCreateTarifHoraire() throws Exception {
            when(tarifHoraireService.save(any(TarifHoraire.class))).thenReturn(testTarif);

            mockMvc.perform(post("/api/tarifs-horaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTarif)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numTarif").value(1));
        }

        @Test
        @DisplayName("PUT /api/tarifs-horaires/{id} - Devrait mettre à jour un tarif horaire")
        void testUpdateTarifHoraire() throws Exception {
            when(tarifHoraireService.existsById(1L)).thenReturn(true);
            when(tarifHoraireService.update(anyLong(), any(TarifHoraire.class))).thenReturn(testTarif);

            mockMvc.perform(put("/api/tarifs-horaires/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTarif)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numTarif").value(1));
        }

        @Test
        @DisplayName("DELETE /api/tarifs-horaires/{id} - Devrait supprimer un tarif horaire")
        void testDeleteTarifHoraire() throws Exception {
            when(tarifHoraireService.existsById(1L)).thenReturn(true);
            doNothing().when(tarifHoraireService).deleteById(1L);

            mockMvc.perform(delete("/api/tarifs-horaires/1"))
                .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Tests du UtilisateurController")
    class UtilisateurControllerTest {
        private Utilisateur testUtilisateur;
        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            
            // Utilisateur de test
            testUtilisateur = new Utilisateur();
            testUtilisateur.setNumUtilisateur(1L);
            testUtilisateur.setNomUtilisateur("Test");
            testUtilisateur.setPrenom("User");
            testUtilisateur.setPseudo("testuser");
            testUtilisateur.setRole(RoleUtilisateur.CLIENT);
            testUtilisateur.setAdresseMail("test@example.com");
            testUtilisateur.setDateDeNaissance(LocalDate.now().minusYears(25));
            testUtilisateur.setMotDePasse("password123");
        }

        @Test
        @DisplayName("GET /api/utilisateurs - Devrait retourner tous les utilisateurs")
        void testGetAllUtilisateurs() throws Exception {
            when(utilisateurService.findAll()).thenReturn(Arrays.asList(testUtilisateur));

            mockMvc.perform(get("/api/utilisateurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numUtilisateur").value(1));
        }

        @Test
        @DisplayName("GET /api/utilisateurs/{id} - Devrait retourner un utilisateur par ID")
        void testGetUtilisateurById() throws Exception {
            when(utilisateurService.findById(1L)).thenReturn(Optional.of(testUtilisateur));

            mockMvc.perform(get("/api/utilisateurs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numUtilisateur").value(1));
        }

        @Test
        @DisplayName("POST /api/utilisateurs - Devrait créer un nouvel utilisateur")
        void testCreateUtilisateur() throws Exception {
            when(utilisateurService.save(any(Utilisateur.class))).thenReturn(testUtilisateur);

            mockMvc.perform(post("/api/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUtilisateur)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numUtilisateur").value(1));
        }

        @Test
        @DisplayName("PUT /api/utilisateurs/{id} - Devrait mettre à jour un utilisateur")
        void testUpdateUtilisateur() throws Exception {
            when(utilisateurService.existsById(1L)).thenReturn(true);
            when(utilisateurService.update(anyLong(), any(Utilisateur.class))).thenReturn(testUtilisateur);

            mockMvc.perform(put("/api/utilisateurs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUtilisateur)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numUtilisateur").value(1));
        }

        @Test
        @DisplayName("DELETE /api/utilisateurs/{id} - Devrait supprimer un utilisateur")
        void testDeleteUtilisateur() throws Exception {
            when(utilisateurService.existsById(1L)).thenReturn(true);
            doNothing().when(utilisateurService).deleteById(1L);

            mockMvc.perform(delete("/api/utilisateurs/1"))
                .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("POST /api/utilisateurs - Devrait rejeter un utilisateur avec nomUtilisateur vide")
        void testCreateUtilisateurWithEmptyNomUtilisateur() throws Exception {
            testUtilisateur.setNomUtilisateur("");
            
            mockMvc.perform(post("/api/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUtilisateur)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /api/utilisateurs - Devrait rejeter un utilisateur avec prenom vide")
        void testCreateUtilisateurWithEmptyPrenom() throws Exception {
            testUtilisateur.setPrenom("");
            
            mockMvc.perform(post("/api/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUtilisateur)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /api/utilisateurs - Devrait rejeter un utilisateur avec pseudo vide")
        void testCreateUtilisateurWithEmptyPseudo() throws Exception {
            testUtilisateur.setPseudo("");
            
            mockMvc.perform(post("/api/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUtilisateur)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /api/utilisateurs - Devrait rejeter un utilisateur avec motDePasse vide")
        void testCreateUtilisateurWithEmptyMotDePasse() throws Exception {
            testUtilisateur.setMotDePasse("");
            
            mockMvc.perform(post("/api/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUtilisateur)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /api/utilisateurs - Devrait rejeter un utilisateur avec email invalide")
        void testCreateUtilisateurWithInvalidEmail() throws Exception {
            testUtilisateur.setAdresseMail("invalid-email");
            
            mockMvc.perform(post("/api/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUtilisateur)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /api/utilisateurs - Devrait rejeter un utilisateur avec date de naissance future")
        void testCreateUtilisateurWithFutureBirthDate() throws Exception {
            testUtilisateur.setDateDeNaissance(LocalDate.now().plusDays(1));
            
            mockMvc.perform(post("/api/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUtilisateur)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /api/utilisateurs - Devrait rejeter un utilisateur avec role null")
        void testCreateUtilisateurWithNullRole() throws Exception {
            testUtilisateur.setRole(null);
            
            mockMvc.perform(post("/api/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUtilisateur)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /api/utilisateurs - Devrait rejeter un utilisateur avec nomUtilisateur trop long")
        void testCreateUtilisateurWithLongNomUtilisateur() throws Exception {
            testUtilisateur.setNomUtilisateur("a".repeat(101));
            
            mockMvc.perform(post("/api/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUtilisateur)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /api/utilisateurs - Devrait rejeter un utilisateur avec pseudo trop long")
        void testCreateUtilisateurWithLongPseudo() throws Exception {
            testUtilisateur.setPseudo("a".repeat(51));
            
            mockMvc.perform(post("/api/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUtilisateur)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /api/utilisateurs - Devrait rejeter un utilisateur avec IBAN trop long")
        void testCreateUtilisateurWithLongIBAN() throws Exception {
            testUtilisateur.setIban("a".repeat(35));
            
            mockMvc.perform(post("/api/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUtilisateur)))
                .andExpect(status().isBadRequest());
        }
    }
} 