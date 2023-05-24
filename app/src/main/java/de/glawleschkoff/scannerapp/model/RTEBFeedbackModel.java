package de.glawleschkoff.scannerapp.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class RTEBFeedbackModel {

    private String id;
    private String zeitstempelScNr;
    private String kurzbefehl;
    private String mitarbeiter;
    private String länge;
    private String breite;
    private String kante;

    public RTEBFeedbackModel(String id, String scannerNr, String kurzbefehl, String mitarbeiter, String länge, String breite, String kante){
        this.id = id;
        this.zeitstempelScNr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Timestamp(System.currentTimeMillis())).concat("#"+scannerNr);
        this.kurzbefehl = kurzbefehl;
        this.mitarbeiter = mitarbeiter;
        this.länge = länge;
        this.breite = breite;
        this.kante = kante;
    }

    public String toCsv(){
        String s = ""
                .concat(id).concat("%")
                .concat(kurzbefehl).concat("%")
                .concat(mitarbeiter).concat("%")
                .concat(zeitstempelScNr).concat("%")
                .concat(länge).concat("%")
                .concat(breite).concat("%")
                .concat(kante);
        return s;
    }

    public String toCsvName(){
        String s = ""
                .concat(id).concat("_")
                .concat(kurzbefehl)
                .concat(".csv");
        return s;
    }
}
