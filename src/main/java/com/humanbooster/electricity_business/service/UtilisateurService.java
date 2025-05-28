package com.humanbooster.electricity_business.service;

import com.humanbooster.electricity_business.model.Role;
import com.humanbooster.electricity_business.model.Utilisateur;
import com.humanbooster.electricity_business.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService extends GenericService<Utilisateur, Long> {

    @Autowired
    protected UtilisateurService(UtilisateurRepository repository) {
        super(repository);
    }

    @Override
    public Utilisateur create(Utilisateur entity) {
        entity.setBanni(false);
        entity.setRole(Role.UTILISATEUR);
        return super.create(entity);
    }
}
