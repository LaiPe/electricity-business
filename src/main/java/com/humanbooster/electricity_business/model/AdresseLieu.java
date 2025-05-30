package com.humanbooster.electricity_business.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("LIEU")
@Data
@EqualsAndHashCode(callSuper = true)
public class AdresseLieu extends Adresse {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lieu_id")
    private Lieu lieu;
}
