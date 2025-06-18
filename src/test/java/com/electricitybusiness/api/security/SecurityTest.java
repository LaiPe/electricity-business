package com.electricitybusiness.api.security;

import com.electricitybusiness.api.model.Lieu;
import com.electricitybusiness.api.model.RoleUtilisateur;
import com.electricitybusiness.api.model.Utilisateur;
import com.electricitybusiness.api.repository.LieuRepository;
import com.electricitybusiness.api.repository.UtilisateurRepository;
import com.electricitybusiness.api.service.JpaUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de sécurité pour l'application Electricity Business.
 * Teste les règles d'accès, l'authentification et les autorisations.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JpaUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LieuRepository lieuRepository;

    private Utilisateur adminUser;
    private Utilisateur proprietaireUser;
    private Utilisateur clientUser;

    @BeforeEach
    void setUp() {
        // Nettoyer la base de données de manière plus robuste
        utilisateurRepository.deleteAll();
        lieuRepository.deleteAll();
        utilisateurRepository.flush(); // Force la synchronisation avec la base de données
        lieuRepository.flush();
        
        // Vérifier que la base est bien vide
        if (utilisateurRepository.count() > 0) {
            throw new RuntimeException("La base de données n'a pas été correctement nettoyée");
        }

        // Créer un lieu de test
        Lieu testLieu = new Lieu();
        testLieu.setInstructions("Lieu de test pour les bornes");
        lieuRepository.save(testLieu);

        // Générer un suffixe unique pour éviter les conflits
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());

        // Créer un utilisateur admin
        adminUser = new Utilisateur();
        adminUser.setNomUtilisateur("Administrateur");
        adminUser.setPrenom("Test");
        adminUser.setPseudo("admin_test_" + uniqueSuffix);
        adminUser.setMotDePasse(passwordEncoder.encode("admin123"));
        adminUser.setAdresseMail("admin_test_" + uniqueSuffix + "@example.com");
        adminUser.setRole(RoleUtilisateur.ADMIN);
        adminUser.setDateDeNaissance(LocalDate.of(1990, 1, 1));
        adminUser.setCompteValide(true);
        adminUser.setBanni(false);
        utilisateurRepository.save(adminUser);

        // Créer un utilisateur propriétaire
        proprietaireUser = new Utilisateur();
        proprietaireUser.setNomUtilisateur("Propriétaire");
        proprietaireUser.setPrenom("Test");
        proprietaireUser.setPseudo("proprietaire_test_" + uniqueSuffix);
        proprietaireUser.setMotDePasse(passwordEncoder.encode("proprietaire123"));
        proprietaireUser.setAdresseMail("proprietaire_test_" + uniqueSuffix + "@example.com");
        proprietaireUser.setRole(RoleUtilisateur.PROPRIETAIRE);
        proprietaireUser.setDateDeNaissance(LocalDate.of(1985, 5, 15));
        proprietaireUser.setCompteValide(true);
        proprietaireUser.setBanni(false);
        utilisateurRepository.save(proprietaireUser);

        // Créer un utilisateur client
        clientUser = new Utilisateur();
        clientUser.setNomUtilisateur("Client");
        clientUser.setPrenom("Test");
        clientUser.setPseudo("client_test_" + uniqueSuffix);
        clientUser.setMotDePasse(passwordEncoder.encode("client123"));
        clientUser.setAdresseMail("client_test_" + uniqueSuffix + "@example.com");
        clientUser.setRole(RoleUtilisateur.CLIENT);
        clientUser.setDateDeNaissance(LocalDate.of(1995, 10, 20));
        clientUser.setCompteValide(true);
        clientUser.setBanni(false);
        utilisateurRepository.save(clientUser);
    }

    @Nested
    @DisplayName("Tests d'accès public")
    class PublicAccessTests {

        @Test
        @DisplayName("GET /api/public/home - Devrait être accessible sans authentification")
        void testPublicHomeAccess() throws Exception {
            mockMvc.perform(get("/api/public/home"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Bienvenue sur l'API Electricity Business"))
                .andExpect(jsonPath("$.status").value("public"));
        }

        @Test
        @DisplayName("GET /api/public/info - Devrait être accessible sans authentification")
        void testPublicInfoAccess() throws Exception {
            mockMvc.perform(get("/api/public/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electricity Business API"));
        }

        @Test
        @DisplayName("POST /api/auth/register - Devrait être accessible sans authentification")
        void testAuthRegisterAccess() throws Exception {
            Map<String, Object> registerRequest = new HashMap<>();
            registerRequest.put("pseudo", "newuser");
            registerRequest.put("password", "password123");
            registerRequest.put("email", "newuser@example.com");
            registerRequest.put("role", "CLIENT");
            registerRequest.put("nomUtilisateur", "New");
            registerRequest.put("prenom", "User");
            registerRequest.put("dateDeNaissance", "1990-01-01");

            mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Utilisateur créé avec succès"));
        }
    }

    @Nested
    @DisplayName("Tests d'accès non authentifié")
    class UnauthenticatedAccessTests {

        @Test
        @DisplayName("GET /api/bornes - Devrait rediriger vers login sans authentification")
        void testBornesAccessWithoutAuth() throws Exception {
            mockMvc.perform(get("/api/bornes"))
                .andExpect(status().is3xxRedirection());
        }

        @Test
        @DisplayName("GET /api/adresses - Devrait rediriger vers login sans authentification")
        void testAdressesAccessWithoutAuth() throws Exception {
            mockMvc.perform(get("/api/adresses"))
                .andExpect(status().is3xxRedirection());
        }

        @Test
        @DisplayName("POST /api/bornes - Devrait rediriger vers login sans authentification")
        void testCreateBorneWithoutAuth() throws Exception {
            Map<String, Object> borneRequest = new HashMap<>();
            borneRequest.put("nomBorne", "Test Borne");

            mockMvc.perform(post("/api/bornes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(borneRequest)))
                .andExpect(status().is3xxRedirection());
        }
    }

    @Nested
    @DisplayName("Tests d'accès avec authentification")
    class AuthenticatedAccessTests {

        @Test
        @WithMockUser(username = "client", roles = {"CLIENT"})
        @DisplayName("GET /api/bornes - Devrait être accessible avec le rôle CLIENT")
        void testBornesAccessWithClientRole() throws Exception {
            mockMvc.perform(get("/api/bornes"))
                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "proprietaire", roles = {"PROPRIETAIRE"})
        @DisplayName("GET /api/adresses - Devrait être accessible avec le rôle PROPRIETAIRE")
        void testAdressesAccessWithProprietaireRole() throws Exception {
            mockMvc.perform(get("/api/adresses"))
                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("POST /api/bornes - Devrait être accessible avec le rôle ADMIN")
        void testCreateBorneWithAdminRole() throws Exception {
            // Récupérer le lieu de test créé dans setUp()
            Lieu testLieu = lieuRepository.findAll().get(0);
            
            Map<String, Object> borneRequest = new HashMap<>();
            borneRequest.put("nomBorne", "Test Borne");
            borneRequest.put("latitude", 48.8566);
            borneRequest.put("longitude", 2.3522);
            borneRequest.put("puissance", 50.0);
            borneRequest.put("etat", "ACTIVE");
            borneRequest.put("lieu", Map.of("numLieu", testLieu.getNumLieu()));

            mockMvc.perform(post("/api/bornes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(borneRequest)))
                .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("Tests d'accès interdit")
    class ForbiddenAccessTests {

        @Test
        @WithMockUser(username = "client", roles = {"CLIENT"})
        @DisplayName("POST /api/bornes - Devrait être interdit avec le rôle CLIENT")
        void testCreateBorneWithClientRole() throws Exception {
            // Récupérer le lieu de test créé dans setUp()
            Lieu testLieu = lieuRepository.findAll().get(0);
            
            Map<String, Object> borneRequest = new HashMap<>();
            borneRequest.put("nomBorne", "Test Borne");
            borneRequest.put("latitude", 48.8566);
            borneRequest.put("longitude", 2.3522);
            borneRequest.put("puissance", 50.0);
            borneRequest.put("etat", "ACTIVE");
            borneRequest.put("lieu", Map.of("numLieu", testLieu.getNumLieu()));

            mockMvc.perform(post("/api/bornes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(borneRequest)))
                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(username = "client", roles = {"CLIENT"})
        @DisplayName("GET /api/adresses - Devrait être interdit avec le rôle CLIENT")
        void testAdressesAccessWithClientRole() throws Exception {
            mockMvc.perform(get("/api/adresses"))
                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(username = "proprietaire", roles = {"PROPRIETAIRE"})
        @DisplayName("DELETE /api/bornes/1 - Devrait être interdit avec le rôle PROPRIETAIRE")
        void testDeleteBorneWithProprietaireRole() throws Exception {
            mockMvc.perform(delete("/api/bornes/1"))
                .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("Tests d'authentification")
    class AuthenticationTests {

        @Test
        @DisplayName("Login avec des credentials valides")
        void testValidLogin() throws Exception {
            mockMvc.perform(post("/login")
                .param("username", adminUser.getPseudo())
                .param("password", "admin123")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());
        }

        @Test
        @DisplayName("Login avec des credentials invalides")
        void testInvalidLogin() throws Exception {
            mockMvc.perform(post("/login")
                .param("username", adminUser.getPseudo())
                .param("password", "wrongpassword")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());
        }

        @Test
        @DisplayName("Logout")
        void testLogout() throws Exception {
            mockMvc.perform(post("/logout"))
                .andExpect(status().is3xxRedirection());
        }
    }

    @Nested
    @DisplayName("Tests de sécurité par méthode")
    class MethodSecurityTests {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("Test d'accès admin aux ressources protégées")
        void testAdminAccessToProtectedResources() throws Exception {
            // Test d'accès aux bornes
            mockMvc.perform(get("/api/bornes"))
                .andExpect(status().isOk());

            // Test d'accès aux adresses
            mockMvc.perform(get("/api/adresses"))
                .andExpect(status().isOk());

            // Test de création de borne
            Lieu testLieu = lieuRepository.findAll().get(0);
            Map<String, Object> borneRequest = new HashMap<>();
            borneRequest.put("nomBorne", "Admin Borne");
            borneRequest.put("latitude", 48.8566);
            borneRequest.put("longitude", 2.3522);
            borneRequest.put("puissance", 50.0);
            borneRequest.put("etat", "ACTIVE");
            borneRequest.put("lieu", Map.of("numLieu", testLieu.getNumLieu()));

            mockMvc.perform(post("/api/bornes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(borneRequest)))
                .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(username = "proprietaire", roles = {"PROPRIETAIRE"})
        @DisplayName("Test d'accès propriétaire aux ressources autorisées")
        void testProprietaireAccessToAuthorizedResources() throws Exception {
            // Test d'accès aux bornes
            mockMvc.perform(get("/api/bornes"))
                .andExpect(status().isOk());

            // Test d'accès aux adresses
            mockMvc.perform(get("/api/adresses"))
                .andExpect(status().isOk());

            // Test de création de borne
            Lieu testLieu = lieuRepository.findAll().get(0);
            Map<String, Object> borneRequest = new HashMap<>();
            borneRequest.put("nomBorne", "Proprietaire Borne");
            borneRequest.put("latitude", 48.8566);
            borneRequest.put("longitude", 2.3522);
            borneRequest.put("puissance", 50.0);
            borneRequest.put("etat", "ACTIVE");
            borneRequest.put("lieu", Map.of("numLieu", testLieu.getNumLieu()));

            mockMvc.perform(post("/api/bornes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(borneRequest)))
                .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(username = "client", roles = {"CLIENT"})
        @DisplayName("Test d'accès client aux ressources limitées")
        void testClientAccessToLimitedResources() throws Exception {
            // Test d'accès aux bornes (autorisé)
            mockMvc.perform(get("/api/bornes"))
                .andExpect(status().isOk());

            // Test d'accès aux adresses (interdit)
            mockMvc.perform(get("/api/adresses"))
                .andExpect(status().isForbidden());

            // Test de création de borne (interdit)
            Lieu testLieu = lieuRepository.findAll().get(0);
            Map<String, Object> borneRequest = new HashMap<>();
            borneRequest.put("nomBorne", "Client Borne");
            borneRequest.put("latitude", 48.8566);
            borneRequest.put("longitude", 2.3522);
            borneRequest.put("puissance", 50.0);
            borneRequest.put("etat", "ACTIVE");
            borneRequest.put("lieu", Map.of("numLieu", testLieu.getNumLieu()));

            mockMvc.perform(post("/api/bornes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(borneRequest)))
                .andExpect(status().isForbidden());
        }
    }
} 