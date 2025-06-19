package com.electricitybusiness.api.config;

import com.electricitybusiness.api.service.JpaUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration de sécurité pour l'application Electricity Business avec JWT.
 * Définit les règles d'accès, l'authentification JWT et les autorisations.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JpaUserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Configuration du SecurityFilterChain avec les règles d'accès et JWT.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Désactiver CSRF pour les API REST
            .csrf(AbstractHttpConfigurer::disable)
            
            // Configuration des autorisations HTTP
            .authorizeHttpRequests(authz -> authz
                // Routes admin
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // Routes pour les propriétaires
                .requestMatchers("/api/proprietaires/**").hasRole("PROPRIETAIRE")
                
                // Routes pour les clients
                .requestMatchers("/api/clients/**").hasRole("CLIENT")

                // Routes d'authentification (publiques)
                .requestMatchers("/api/auth/**").permitAll()

                // Routes publiques
                .requestMatchers("/", "/home", "/api/public/**").permitAll()
                
                // Toutes les autres routes nécessitent une authentification
                .anyRequest().authenticated()
            )
            
            // Configuration de la session comme STATELESS (pour JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Configuration du provider d'authentification
            .authenticationProvider(authenticationProvider())
            
            // Ajouter le filtre JWT avant UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
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
    public AuthenticationProvider authenticationProvider() {
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