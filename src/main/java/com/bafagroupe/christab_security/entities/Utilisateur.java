package com.bafagroupe.christab_security.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
public class Utilisateur {
    private int idUtilisateur;
    private int idTypeFonction;
    private String email;
    private String nom;
    private String prenom;
    private Long tel;
    private String photo;
    private String cnib;
    private String numeroCnib;
    private String dateDelivrance;
    private String dateExpiration;
    private String dateInscription;
    private String lieuDelivrance;
    private String typeDocument;
    private Long telSos;
}
