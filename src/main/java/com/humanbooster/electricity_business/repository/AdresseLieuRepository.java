package com.humanbooster.electricity_business.repository;

import com.humanbooster.electricity_business.model.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdresseLieuRepository extends JpaRepository<Adresse, Integer> {
}
