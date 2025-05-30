package com.humanbooster.electricity_business.repository;

import com.humanbooster.electricity_business.model.AdresseFacturation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdresseFacturationRepository extends JpaRepository<AdresseFacturation, Long> {
}
