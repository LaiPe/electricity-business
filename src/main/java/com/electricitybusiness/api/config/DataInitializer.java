package com.electricitybusiness.api.config;

import com.electricitybusiness.api.model.Lieu;
import com.electricitybusiness.api.model.RoleUtilisateur;
import com.electricitybusiness.api.model.Utilisateur;
import com.electricitybusiness.api.repository.LieuRepository;
import com.electricitybusiness.api.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Initialiseur de données pour créer des utilisateurs de test au démarrage.
 * Ne s'exécute qu'en mode développement.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test") // Ne s'exécute pas en mode test
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Vérifier si des utilisateurs existent déjà
        if (utilisateurRepository.count() == 0) {
            log.info("Initialisation des données de test...");
            createTestUsers();
            log.info("Données de test initialisées avec succès");
        } else {
            log.info("Des utilisateurs existent déjà, pas d'initialisation nécessaire");
        }
    }

    /**
     * Crée les utilisateurs de test.
     */
    private void createTestUsers() {
        // Créer un utilisateur admin
        Utilisateur adminUser = new Utilisateur();
        adminUser.setNomUtilisateur("Administrateur");
        adminUser.setPrenom("Admin");
        adminUser.setPseudo("admin");
        adminUser.setMotDePasse(passwordEncoder.encode("admin123"));
        adminUser.setAdresseMail("admin@electricitybusiness.com");
        adminUser.setRole(RoleUtilisateur.ADMIN);
        adminUser.setDateDeNaissance(LocalDate.of(1990, 1, 1));
        adminUser.setCompteValide(true);
        adminUser.setBanni(false);
        utilisateurRepository.save(adminUser);
        log.info("Utilisateur admin créé: admin/admin123");

        // Créer un utilisateur propriétaire
        Utilisateur proprietaireUser = new Utilisateur();
        proprietaireUser.setNomUtilisateur("Propriétaire");
        proprietaireUser.setPrenom("Test");
        proprietaireUser.setPseudo("proprietaire");
        proprietaireUser.setMotDePasse(passwordEncoder.encode("proprietaire123"));
        proprietaireUser.setAdresseMail("proprietaire@electricitybusiness.com");
        proprietaireUser.setRole(RoleUtilisateur.PROPRIETAIRE);
        proprietaireUser.setDateDeNaissance(LocalDate.of(1985, 5, 15));
        proprietaireUser.setCompteValide(true);
        proprietaireUser.setBanni(false);
        utilisateurRepository.save(proprietaireUser);
        log.info("Utilisateur propriétaire créé: proprietaire/proprietaire123");

        // Créer un utilisateur client
        Utilisateur clientUser = new Utilisateur();
        clientUser.setNomUtilisateur("Client");
        clientUser.setPrenom("Test");
        clientUser.setPseudo("client");
        clientUser.setMotDePasse(passwordEncoder.encode("client123"));
        clientUser.setAdresseMail("client@electricitybusiness.com");
        clientUser.setRole(RoleUtilisateur.CLIENT);
        clientUser.setDateDeNaissance(LocalDate.of(1995, 10, 20));
        clientUser.setCompteValide(true);
        clientUser.setBanni(false);
        utilisateurRepository.save(clientUser);
        log.info("Utilisateur client créé: client/client123");
    }
} 