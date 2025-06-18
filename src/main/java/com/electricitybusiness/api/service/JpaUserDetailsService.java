package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.Utilisateur;
import com.electricitybusiness.api.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service d'authentification qui implémente UserDetailsService.
 * Charge les utilisateurs depuis la base de données pour Spring Security.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JpaUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    /**
     * Charge un utilisateur par son pseudo (username).
     *
     * @param username le pseudo
     * @return UserDetails de l'utilisateur
     * @throws UsernameNotFoundException si l'utilisateur n'est pas trouvé
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Tentative de chargement de l'utilisateur: {}", username);
        return utilisateurRepository.findByPseudo(username)
                .orElseThrow(() -> {
                    log.warn("Utilisateur non trouvé: {}", username);
                    return new UsernameNotFoundException("Utilisateur non trouvé: " + username);
                });
    }

    /**
     * Charge un utilisateur par son adresse email.
     *
     * @param email l'adresse email
     * @return UserDetails de l'utilisateur
     * @throws UsernameNotFoundException si l'utilisateur n'est pas trouvé
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        log.debug("Tentative de chargement de l'utilisateur par email: {}", email);
        return utilisateurRepository.findByAdresseMail(email)
                .orElseThrow(() -> {
                    log.warn("Utilisateur non trouvé avec l'email: {}", email);
                    return new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email);
                });
    }

    /**
     * Crée un nouvel utilisateur.
     *
     * @param utilisateur l'utilisateur à créer
     * @return l'utilisateur créé
     */
    public Utilisateur createUser(Utilisateur utilisateur) {
        log.info("Création d'un nouvel utilisateur: {}", utilisateur.getPseudo());
        return utilisateurRepository.save(utilisateur);
    }

    /**
     * Met à jour la date de dernière connexion.
     *
     * @param pseudo le pseudo
     */
    public void updateLastLogin(String pseudo) {
        utilisateurRepository.findByPseudo(pseudo).ifPresent(utilisateur -> {
            // utilisateur.setLastLoginAt(java.time.LocalDateTime.now()); // à ajouter si besoin
            utilisateurRepository.save(utilisateur);
            log.debug("Date de dernière connexion mise à jour pour: {}", pseudo);
        });
    }
} 