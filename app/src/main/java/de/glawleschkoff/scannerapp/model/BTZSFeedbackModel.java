package de.glawleschkoff.scannerapp.model;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class BTZSFeedbackModel {

    private String exemplarNr;
    private String zeitstempelScNr;
    private String kurzbefehl;
    private String mitarbeiter;
    private String optionen;

    public BTZSFeedbackModel(String exemplarNr, String scannerNr, String kurzbefehl, String mitarbeiter, String optionen){
        this.exemplarNr = exemplarNr;
        this.kurzbefehl = kurzbefehl;
        this.mitarbeiter = mitarbeiter;
        this.optionen = optionen;
        this.zeitstempelScNr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Timestamp(System.currentTimeMillis())).concat("#"+scannerNr);
    }

    public String toCsv(){
        String s = ""
                .concat(exemplarNr).concat(";")
                .concat(kurzbefehl).concat(";")
                .concat(mitarbeiter).concat(";")
                .concat(optionen).concat(";")
                .concat(zeitstempelScNr);
        return s;
    }

    public String toCsvName(){
        String s = ""
                .concat(exemplarNr).concat("_")
                .concat(kurzbefehl)
                .concat(".csv");
        return s;
    }

    public String toString(){
        String s = ""
                .concat("ExemplarNr: ").concat(exemplarNr).concat("\n")
                .concat("Kurzbefehl: ").concat(kurzbefehl).concat("\n")
                .concat("Mitarbeiter: ").concat(mitarbeiter).concat("\n")
                .concat("Optionen: ").concat(optionen).concat("\n")
                .concat("ZeitstempelScNr: ").concat(zeitstempelScNr);
        return s;
    }

}
