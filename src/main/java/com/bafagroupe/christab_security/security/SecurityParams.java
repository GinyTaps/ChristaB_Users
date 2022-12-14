package com.bafagroupe.christab_security.security;

public class SecurityParams {
    public static final String SECRET = "xxxxxxxxxxx";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_HEADER_NAME = "Authorization";
    public static final long TIME = 864000000; // System.currentTimeMillis()+10*24*3600*1000; // 10 jours * 24 heures * 3600 secondes * 1000 millisecondes
    public static final String CLAIM = "Roles";
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    public static final String EMAIL_FROM = "xxxxxxxxxx";
    public static final String EMAIL_CONTENT = "Pour activer votre compte ChristaB, veuillez cliquer sur ce lien: http://localhost:8087/api/confirmAccount?token=";
    public static final String EMAIL_VALIDATE = "Votre compte est en cours de validation!";
    public static final String EMAIL_SUBJECT = "Validation du compte ChristaB";
    public static final String EMAIL_REACTIVATE = "Pour pouvoir utiliser à nouveau votre compte, veuillez cliquer sur ce lien pour confirmer ce nouvel email http://localhost:8087/api/confirmAccount?token=";
    public static final String USERNAME = "xxxxxxxxxxxxx";
    public static final String HOST = "xxxxxxxxxx";
    public static final int PORT = 587;
    public static final String PASSWORD = "xxxxxxxxx";
    public static String emailRecup ="";
    public static String ACTIVATED_ACCOUNT_MESSAGE="<div>======================== <strong> SUCCES </strong> =======================<br>" +
                                                    "====================================================== vous pouvez maintenant accéder à l'application<br> </div>";
    public static final String MESSAGE = "Bonjour, vous avez demandé à ré-initialiser votre mot de passe? Si oui, veuillez entrer ce code:"
            + " ";
}
