package de.glawleschkoff.scannerapp.model;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class FeedbackModel {

    private String exemplarNr;
    private String zeitstempelScNr;
    private String kurzbefehl;
    private String mitarbeiter;
    private String optionen;

    public FeedbackModel(){
        this.exemplarNr = null;
        this.kurzbefehl = null;
        this.mitarbeiter = null;
        this.optionen = null;
        this.zeitstempelScNr = null;
    }

    public FeedbackModel(String exemplarNr, String scannerNr, String kurzbefehl, String mitarbeiter, String optionen){
        this.exemplarNr = exemplarNr;
        this.kurzbefehl = kurzbefehl;
        this.mitarbeiter = mitarbeiter;
        this.optionen = optionen;
        this.zeitstempelScNr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Timestamp(System.currentTimeMillis())).concat("#"+scannerNr);
    }

    public FeedbackModel(String csv){
        List<String> valueList = Arrays.asList(csv.split(";"));
        this.exemplarNr = valueList.get(0);
        this.kurzbefehl = valueList.get(1);
        this.mitarbeiter = valueList.get(2);
        this.optionen = valueList.get(3);
        this.zeitstempelScNr = valueList.get(4);
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
