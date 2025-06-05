package com.electricitybusiness.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application Electricity Business.
 * 
 * Cette application gère les bornes électriques, les utilisateurs,
 * les réservations et les tarifs horaires.
 */
@SpringBootApplication
public class ElectricityBusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElectricityBusinessApplication.class, args);
    }
} 