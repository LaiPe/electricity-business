package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.RoleUtilisateur;
import com.electricitybusiness.api.model.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Tests du JwtService")
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;
    
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        // Créer un utilisateur de test
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNumUtilisateur(1L);
        utilisateur.setPseudo("testuser");
        utilisateur.setRole(RoleUtilisateur.CLIENT);
        utilisateur.setCompteValide(true);
        utilisateur.setBanni(false);
        
        userDetails = utilisateur;
    }

    @Test
    @DisplayName("Test de génération de token JWT")
    void testGenerateToken() {
        // Act
        String token = jwtService.generateToken("testuser");

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // Format JWT: header.payload.signature
    }

    @Test
    @DisplayName("Test de génération de token avec claims supplémentaires")
    void testGenerateTokenWithExtraClaims() {
        // Arrange
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        extraClaims.put("userId", 123);

        // Act
        String token = jwtService.generateToken(extraClaims, "testuser");

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Test d'extraction du nom d'utilisateur")
    void testExtractUsername() {
        // Arrange
        String token = jwtService.generateToken("testuser");

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals("testuser", username);
    }

    @Test
    @DisplayName("Test d'extraction de la date d'expiration")
    void testExtractExpiration() {
        // Arrange
        String token = jwtService.generateToken("testuser");

        // Act
        Date expiration = jwtService.extractExpiration(token);

        // Assert
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date())); // L'expiration doit être dans le futur
    }

    @Test
    @DisplayName("Test de validation de token valide")
    void testIsTokenValidWithValidToken() {
        // Arrange
        String token = jwtService.generateToken("testuser");

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Test de validation de token avec mauvais utilisateur")
    void testIsTokenValidWithWrongUser() {
        // Arrange
        String token = jwtService.generateToken("testuser");
        
        Utilisateur wrongUser = new Utilisateur();
        wrongUser.setPseudo("wronguser");
        wrongUser.setRole(RoleUtilisateur.CLIENT);
        wrongUser.setCompteValide(true);
        wrongUser.setBanni(false);

        // Act
        boolean isValid = jwtService.isTokenValid(token, wrongUser);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Test d'extraction de claim personnalisée")
    void testExtractClaim() {
        // Arrange
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        String token = jwtService.generateToken(extraClaims, "testuser");

        // Act
        String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));

        // Assert
        assertEquals("ADMIN", role);
    }
} 