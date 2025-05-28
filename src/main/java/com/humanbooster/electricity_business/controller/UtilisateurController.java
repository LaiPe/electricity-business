package com.humanbooster.electricity_business.controller;

import com.humanbooster.electricity_business.model.Utilisateur;
import com.humanbooster.electricity_business.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UtilisateurController extends GenericController<Utilisateur, Long> {
    private final UtilisateurService service;

    @Autowired
    protected UtilisateurController(UtilisateurService utilisateurService) {
        super(utilisateurService);
        this.service = utilisateurService;
    }

    @Override
    @GetMapping("/utilisateurs")
    public List<Utilisateur> getAll() {
        return super.getAll();
    }

    @Override
    @PostMapping("/utilisateurs")
    public Utilisateur create(@RequestBody Utilisateur utilisateur) {
        return super.create(utilisateur);
    }

    @Override
    @GetMapping("/utilisateurs/{id}")
    public ResponseEntity<Utilisateur> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @DeleteMapping("/utilisateurs/{id}")
    public ResponseEntity<Utilisateur> deleteById(@PathVariable Long id) {
        return super.deleteById(id);
    }

    @Override
    @PutMapping("/utilisateurs/{id}")
    public ResponseEntity<Utilisateur> updateById(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        return super.updateById(id, utilisateur);
    }

}
