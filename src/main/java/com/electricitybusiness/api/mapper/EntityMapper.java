package com.electricitybusiness.api.mapper;

import com.electricitybusiness.api.dto.*;
import com.electricitybusiness.api.model.*;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre entités JPA et DTOs
 * Évite les références circulaires en contrôlant la sérialisation
 */
@Component
public class EntityMapper {

    // === LIEU ===
    public LieuDTO toDTO(Lieu lieu) {
        if (lieu == null) return null;
        return new LieuDTO(lieu.getNumLieu(), lieu.getInstructions());
    }

    public Lieu toEntity(LieuDTO dto) {
        if (dto == null) return null;
        Lieu lieu = new Lieu();
        lieu.setNumLieu(dto.getNumLieu());
        lieu.setInstructions(dto.getInstructions());
        return lieu;
    }

    // === ADRESSE ===
    public AdresseDTO toDTO(Adresse adresse) {
        if (adresse == null) return null;
        return new AdresseDTO(
            adresse.getNumAdresse(),
            adresse.getNomAdresse(),
            adresse.getNumeroEtRue(),
            adresse.getCodePostal(),
            adresse.getVille(),
            adresse.getPays(),
            adresse.getRegion(),
            adresse.getComplement(),
            adresse.getEtage(),
            toDTO(adresse.getLieu()) // Référence simple
        );
    }

    public Adresse toEntity(AdresseDTO dto) {
        if (dto == null) return null;
        Adresse adresse = new Adresse();
        adresse.setNumAdresse(dto.getNumAdresse());
        adresse.setNomAdresse(dto.getNomAdresse());
        adresse.setNumeroEtRue(dto.getNumeroEtRue());
        adresse.setCodePostal(dto.getCodePostal());
        adresse.setVille(dto.getVille());
        adresse.setPays(dto.getPays());
        adresse.setRegion(dto.getRegion());
        adresse.setComplement(dto.getComplement());
        adresse.setEtage(dto.getEtage());
        adresse.setLieu(toEntity(dto.getLieu()));
        return adresse;
    }

    // === UTILISATEUR ===
    public UtilisateurDTO toDTO(Utilisateur utilisateur) {
        if (utilisateur == null) return null;
        return new UtilisateurDTO(
            utilisateur.getNumUtilisateur(),
            utilisateur.getNomUtilisateur(),
            utilisateur.getPrenom(),
            utilisateur.getPseudo(),
            utilisateur.getRole(),
            utilisateur.getAdresseMail(),
            utilisateur.getDateDeNaissance(),
            utilisateur.getIban(),
            utilisateur.getVehicule(),
            utilisateur.getBanni(),
            utilisateur.getCompteValide(),
            utilisateur.getDateInscription(),
            toDTO(utilisateur.getLieu())
        );
    }

    public Utilisateur toEntity(UtilisateurDTO dto) {
        if (dto == null) return null;
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNumUtilisateur(dto.getNumUtilisateur());
        utilisateur.setNomUtilisateur(dto.getNomUtilisateur());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setPseudo(dto.getPseudo());
        utilisateur.setRole(dto.getRole());
        utilisateur.setAdresseMail(dto.getAdresseMail());
        utilisateur.setDateDeNaissance(dto.getDateDeNaissance());
        utilisateur.setIban(dto.getIban());
        utilisateur.setVehicule(dto.getVehicule());
        utilisateur.setBanni(dto.getBanni());
        utilisateur.setCompteValide(dto.getCompteValide());
        utilisateur.setDateInscription(dto.getDateInscription());
        utilisateur.setLieu(toEntity(dto.getLieu()));
        return utilisateur;
    }

    // === BORNE ===
    public BorneDTO toDTO(Borne borne) {
        if (borne == null) return null;
        return new BorneDTO(
            borne.getNumBorne(),
            borne.getNomBorne(),
            borne.getLatitude(),
            borne.getLongitude(),
            borne.getPuissance(),
            borne.getInstruction(),
            borne.getSurPied(),
            borne.getEtat(),
            borne.getOccupee(),
            borne.getDateCreation(),
            borne.getDerniereMaintenance(),
            toDTO(borne.getLieu())
        );
    }

    public Borne toEntity(BorneDTO dto) {
        if (dto == null) return null;
        Borne borne = new Borne();
        borne.setNumBorne(dto.getNumBorne());
        borne.setNomBorne(dto.getNomBorne());
        borne.setLatitude(dto.getLatitude());
        borne.setLongitude(dto.getLongitude());
        borne.setPuissance(dto.getPuissance());
        borne.setInstruction(dto.getInstruction());
        borne.setSurPied(dto.getSurPied());
        borne.setEtat(dto.getEtat());
        borne.setOccupee(dto.getOccupee());
        borne.setDateCreation(dto.getDateCreation());
        borne.setDerniereMaintenance(dto.getDerniereMaintenance());
        borne.setLieu(toEntity(dto.getLieu()));
        return borne;
    }

    // === MEDIA ===
    public MediaDTO toDTO(Media media) {
        if (media == null) return null;
        return new MediaDTO(
            media.getNumMedia(),
            media.getNomMedia(),
            media.getType(),
            media.getUrl(),
            media.getDescription(),
            null
        );
    }

    public Media toEntity(MediaDTO dto) {
        if (dto == null) return null;
        Media media = new Media();
        media.setNumMedia(dto.getNumMedia());
        media.setNomMedia(dto.getNomMedia());
        media.setType(dto.getTypeMedia());
        media.setUrl(dto.getUrlMedia());
        media.setDescription(dto.getDescription());
        return media;
    }

    // === RESERVATION ===
    public ReservationDTO toDTO(Reservation reservation) {
        if (reservation == null) return null;
        return new ReservationDTO(
            reservation.getNumReservation(),
            reservation.getDateDebut(),
            reservation.getDateFin(),
            reservation.getEtat(),
            reservation.getMontantTotal(),
            reservation.getDateValidation(),
            reservation.getUtilisateur() != null ? reservation.getUtilisateur().getNumUtilisateur() : null,
            reservation.getBorne() != null ? reservation.getBorne().getNumBorne() : null
        );
    }

    public Reservation toEntity(ReservationDTO dto) {
        if (dto == null) return null;
        Reservation reservation = new Reservation();
        reservation.setNumReservation(dto.getId());
        reservation.setDateDebut(dto.getDateDebutReservation());
        reservation.setDateFin(dto.getDateFinReservation());
        reservation.setEtat(dto.getEtatReservation());
        reservation.setMontantTotal(dto.getMontantPaye());
        reservation.setDateValidation(dto.getDatePaiement());
        // Note: Les références Utilisateur et Borne doivent être résolues par le service
        return reservation;
    }

    // === TARIF HORAIRE ===
    public TarifHoraireDTO toDTO(TarifHoraire tarif) {
        if (tarif == null) return null;
        return new TarifHoraireDTO(
            tarif.getNumTarif(),
            tarif.getTarifParMinute(),
            tarif.getHeureDebut(),
            tarif.getHeureFin(),
            tarif.getJourSemaine(),
            tarif.getDateDebut(),
            tarif.getDateFin(),
            tarif.getActif(),
            tarif.getBorne() != null ? tarif.getBorne().getNumBorne() : null
        );
    }

    public TarifHoraire toEntity(TarifHoraireDTO dto) {
        if (dto == null) return null;
        TarifHoraire tarif = new TarifHoraire();
        tarif.setNumTarif(dto.getNumTarif());
        tarif.setTarifParMinute(dto.getTarifParMinute());
        tarif.setHeureDebut(dto.getHeureDebut());
        tarif.setHeureFin(dto.getHeureFin());
        tarif.setJourSemaine(dto.getJourSemaine());
        tarif.setDateDebut(dto.getDateDebut());
        tarif.setDateFin(dto.getDateFin());
        tarif.setActif(dto.getActif());
        // Note: La référence Borne doit être résolue par le service
        return tarif;
    }
} 