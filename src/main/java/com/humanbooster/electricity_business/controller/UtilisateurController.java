package com.humanbooster.electricity_business.controller;

import com.humanbooster.electricity_business.model.Utilisateur;
import com.humanbooster.electricity_business.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/utilisateurs")
public class UtilisateurController extends GenericController<Utilisateur, Long> {
    private final UtilisateurService service;

    @Autowired
    protected UtilisateurController(UtilisateurService utilisateurService) {
        super(utilisateurService);
        this.service = utilisateurService;
    }
}
