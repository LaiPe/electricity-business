package com.electricitybusiness.api.controller;

import com.electricitybusiness.api.model.RoleUtilisateur;
import com.electricitybusiness.api.model.Utilisateur;
import com.electricitybusiness.api.service.JpaUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur pour l'authentification et l'inscription des utilisateurs.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JpaUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Endpoint d'inscription d'un nouvel utilisateur.
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Tentative d'inscription pour l'utilisateur: {}", request.getPseudo());
        try {
            // Créer un nouvel utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setPseudo(request.getPseudo());
            utilisateur.setMotDePasse(passwordEncoder.encode(request.getPassword()));
            utilisateur.setAdresseMail(request.getEmail());
            utilisateur.setRole(request.getRole() != null ? request.getRole() : RoleUtilisateur.CLIENT);
            utilisateur.setNomUtilisateur(request.getNomUtilisateur());
            utilisateur.setPrenom(request.getPrenom());
            utilisateur.setDateDeNaissance(LocalDate.parse(request.getDateDeNaissance(), DateTimeFormatter.ISO_DATE));
            utilisateur.setCompteValide(true);
            utilisateur.setBanni(false);

            Utilisateur savedUtilisateur = userDetailsService.createUser(utilisateur);

            Map<String, Object> response = new HashMap<>();
            response.put("userId", savedUtilisateur.getNumUtilisateur());
            response.put("pseudo", savedUtilisateur.getPseudo());
            response.put("role", savedUtilisateur.getRole());
            response.put("message", "Utilisateur créé avec succès");
            log.info("Utilisateur créé avec succès: {}", savedUtilisateur.getPseudo());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Erreur lors de la création de l'utilisateur: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erreur lors de la création de l'utilisateur");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Endpoint pour vérifier le statut d'authentification.
     * GET /api/auth/status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getAuthStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", true);
        response.put("message", "Utilisateur authentifié");
        return ResponseEntity.ok(response);
    }

    /**
     * Classe interne pour la requête d'inscription.
     */
    public static class RegisterRequest {
        private String pseudo;
        private String password;
        private String email;
        private RoleUtilisateur role;
        private String nomUtilisateur;
        private String prenom;
        private String dateDeNaissance;

        public String getPseudo() { return pseudo; }
        public void setPseudo(String pseudo) { this.pseudo = pseudo; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public RoleUtilisateur getRole() { return role; }
        public void setRole(RoleUtilisateur role) { this.role = role; }
        public String getNomUtilisateur() { return nomUtilisateur; }
        public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }
        public String getPrenom() { return prenom; }
        public void setPrenom(String prenom) { this.prenom = prenom; }
        public String getDateDeNaissance() { return dateDeNaissance; }
        public void setDateDeNaissance(String dateDeNaissance) { this.dateDeNaissance = dateDeNaissance; }
    }
} 