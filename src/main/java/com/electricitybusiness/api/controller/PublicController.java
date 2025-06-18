package com.electricitybusiness.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur pour les routes publiques accessibles sans authentification.
 */
@RestController
@RequestMapping("/api/public")
public class PublicController {

    /**
     * Page d'accueil publique.
     * GET /api/public/home
     */
    @GetMapping("/home")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bienvenue sur l'API Electricity Business");
        response.put("version", "1.0.0");
        response.put("status", "public");
        return response;
    }

    /**
     * Informations sur l'API.
     * GET /api/public/info
     */
    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "Electricity Business API");
        response.put("description", "API REST pour gestion des bornes de recharge électrique");
        response.put("endpoints", new String[]{
            "/api/public/home",
            "/api/public/info",
            "/api/auth/register",
            "/api/auth/status"
        });
        return response;
    }
} 