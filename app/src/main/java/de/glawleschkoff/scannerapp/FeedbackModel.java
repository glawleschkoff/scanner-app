package de.glawleschkoff.scannerapp;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class FeedbackModel {

    private String rowTimestamp;
    private String rowCreationTimestamp;
    private String exemplarNr;
    private String zeitstempelScNr;
    private String kurzbefehl;
    private String mitarbeiter;
    private String optionen;

    public FeedbackModel(String zeitstempelScNr){
        this.zeitstempelScNr = zeitstempelScNr;
        exemplarNr = "";
        kurzbefehl = "";
        mitarbeiter = "";
        optionen = "";
    }

    public FeedbackModel(String exemplarNr, String scannerNr, String kurzbefehl, String mitarbeiter, String optionen){
        this.exemplarNr = exemplarNr;
        this.kurzbefehl = kurzbefehl;
        this.mitarbeiter = mitarbeiter;
        this.optionen = optionen;
        this.zeitstempelScNr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Timestamp(System.currentTimeMillis())).concat("#"+scannerNr);
        this.rowTimestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").format(new Timestamp(System.currentTimeMillis()));
        this.rowCreationTimestamp = rowTimestamp;
    }

}
