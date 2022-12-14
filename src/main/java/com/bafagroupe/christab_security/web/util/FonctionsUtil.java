package com.bafagroupe.christab_security.web.util;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

@Component
public class FonctionsUtil implements Serializable {

    /**************** Génération d'un code de 6 chiffres pour ré-initialiser le mot de passe **************/
    public int getRandomDigitCode() {
        Random rd = new Random();
        int code = 100000 + rd.nextInt(900000);
        /*System.out.println("************* Code a 6 chiffres new ***********");
        System.out.println(code);*/
        return code;
    }

    public Timestamp getCurrentDateTime() {
        Date date= new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);

        return ts;
    }
}
