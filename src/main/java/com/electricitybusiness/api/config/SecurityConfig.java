package com.electricitybusiness.api.config;

import com.electricitybusiness.api.service.JpaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

/**
 * Configuration de sécurité pour l'application Electricity Business.
 * Définit les règles d'accès, l'authentification et les autorisations.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JpaUserDetailsService userDetailsService;

    public SecurityConfig(JpaUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configuration du SecurityFilterChain avec les règles d'accès.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Désactiver CSRF pour les API REST (optionnel selon les besoins)
            .csrf(AbstractHttpConfigurer::disable)
            
            // Configuration des autorisations HTTP
            .authorizeHttpRequests(authz -> authz
                // Routes admin
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // Routes pour les propriétaires
                .requestMatchers("/api/proprietaires/**").hasRole("PROPRIETAIRE")
                
                // Routes pour les clients
                .requestMatchers("/api/clients/**").hasRole("CLIENT")
                
                // Routes d'authentification
                .requestMatchers("/api/auth/**").permitAll()

                // Routes publiques
                .requestMatchers("/", "/home", "/api/public/**").permitAll()
                
                // Toutes les autres routes nécessitent une authentification
                .anyRequest().authenticated()
            )
            
            // Configuration du formulaire de login
            .formLogin(Customizer.withDefaults())
            
            // Configuration de la déconnexion
            .logout(Customizer.withDefaults())
            
            // Configuration du provider d'authentification
            .authenticationProvider(authenticationProvider());
        
        return http.build();
    }

    /**
     * Bean pour l'encodage des mots de passe.
     * Utilise BCrypt pour un encodage sécurisé.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provider d'authentification DAO.
     * Configure l'authentification avec la base de données.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Manager d'authentification.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
} 