package com.electricitybusiness.api.controller;

import com.electricitybusiness.api.model.RoleUtilisateur;
import com.electricitybusiness.api.model.Utilisateur;
import com.electricitybusiness.api.service.JpaUserDetailsService;
import com.electricitybusiness.api.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JpaUserDetailsService userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;

    private Utilisateur testUtilisateur;
    private Authentication testAuthentication;

    @BeforeEach
    void setUp() {
        // Créer un utilisateur de test
        testUtilisateur = new Utilisateur();
        testUtilisateur.setNumUtilisateur(1L);
        testUtilisateur.setPseudo("testuser");
        testUtilisateur.setMotDePasse("encodedPassword");
        testUtilisateur.setRole(RoleUtilisateur.CLIENT);
        testUtilisateur.setAdresseMail("test@example.com");
        testUtilisateur.setNomUtilisateur("Test");
        testUtilisateur.setPrenom("User");
        testUtilisateur.setDateDeNaissance(LocalDate.of(1990, 1, 1));
        testUtilisateur.setCompteValide(true);
        testUtilisateur.setBanni(false);

        // Créer une authentification de test
        testAuthentication = new UsernamePasswordAuthenticationToken(
            testUtilisateur, null, testUtilisateur.getAuthorities()
        );
    }

    @Test
    @DisplayName("POST /api/auth/login - Devrait retourner un token JWT avec des credentials valides")
    void testLoginWithValidCredentials() throws Exception {
        // Arrange
        AuthController.AuthRequest request = new AuthController.AuthRequest("testuser", "password123");
        String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(testAuthentication);
        when(jwtService.generateToken("testuser")).thenReturn(expectedToken);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedToken))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.message").value("Authentification réussie"))
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken("testuser");
    }

    @Test
    @DisplayName("POST /api/auth/login - Devrait retourner 401 avec des credentials invalides")
    void testLoginWithInvalidCredentials() throws Exception {
        // Arrange
        AuthController.AuthRequest request = new AuthController.AuthRequest("testuser", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Identifiants invalides"))
                .andExpect(jsonPath("$.message").value("Nom d'utilisateur ou mot de passe incorrect"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtService);
    }

    @Test
    @DisplayName("POST /api/auth/register - Devrait créer un nouvel utilisateur")
    void testRegisterNewUser() throws Exception {
        // Arrange
        AuthController.RegisterRequest request = new AuthController.RegisterRequest();
        request.setPseudo("newuser");
        request.setPassword("password123");
        request.setEmail("newuser@example.com");
        request.setRole(RoleUtilisateur.CLIENT);
        request.setNomUtilisateur("New");
        request.setPrenom("User");
        request.setDateDeNaissance("1990-01-01");

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userDetailsService.createUser(any(Utilisateur.class))).thenReturn(testUtilisateur);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.pseudo").value("testuser"))
                .andExpect(jsonPath("$.role").value("CLIENT"))
                .andExpect(jsonPath("$.message").value("Utilisateur créé avec succès"));

        verify(passwordEncoder).encode("password123");
        verify(userDetailsService).createUser(any(Utilisateur.class));
    }

    @Test
    @DisplayName("POST /api/auth/register - Devrait utiliser CLIENT comme rôle par défaut si non spécifié")
    void testRegisterWithDefaultRole() throws Exception {
        // Arrange
        AuthController.RegisterRequest request = new AuthController.RegisterRequest();
        request.setPseudo("newuser");
        request.setPassword("password123");
        request.setEmail("newuser@example.com");
        request.setNomUtilisateur("New");
        request.setPrenom("User");
        request.setDateDeNaissance("1990-01-01");
        // Role non défini

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userDetailsService.createUser(any(Utilisateur.class))).thenReturn(testUtilisateur);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(userDetailsService).createUser(argThat(user -> 
            user.getRole() == RoleUtilisateur.CLIENT
        ));
    }
} 