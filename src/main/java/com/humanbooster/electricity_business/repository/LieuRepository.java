package com.humanbooster.electricity_business.repository;

import com.humanbooster.electricity_business.model.Lieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LieuRepository extends JpaRepository<Lieu, Long> {
}
