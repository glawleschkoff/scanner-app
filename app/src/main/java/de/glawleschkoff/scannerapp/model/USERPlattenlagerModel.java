package de.glawleschkoff.scannerapp.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

public class USERPlattenlagerModel {
    private Integer rowID;
    private Timestamp rowTimestamp;
    private Integer rowUserID;
    private UUID rowGUID;
    private Timestamp rowCreationTimestamp;
    private Date rowEarliestDelDate;
    private Date rowLatestDelDate;
    private Double plattenID;
    private String lagerPlatz;
    private Double lng;
    private Double brt;
    private String matKurzzeichen;
    private String mz3;
    private Double optimiert;
    private Double produktion;
    private String mz6;
    private String mz1;
    private String mzd1;
    private Double menge;
    private String mzd2;

    public USERPlattenlagerModel(Double plattenID, String lagerPlatz, Double lng, Double brt, String matKurzzeichen, String mz3, String auslagerId, String auslagerInfo, String auslagerDatum, Double menge, String einlagerDatum){
        this.plattenID = plattenID;
        this.lagerPlatz = lagerPlatz;
        this.lng = lng;
        this.brt = brt;
        this.matKurzzeichen = matKurzzeichen;
        this.mz3 = mz3;
        this.mz6 = auslagerId;
        this.mz1 = auslagerInfo;
        this.mzd1 = auslagerDatum;
        this.menge = menge;
        this.mzd2 = einlagerDatum;
    }

    public USERPlattenlagerModel(Double plattenID, String lagerPlatz, Double lng, Double brt, String matKurzzeichen, String mz3){
        this.plattenID = plattenID;
        this.lagerPlatz = lagerPlatz;
        this.lng = lng;
        this.brt = brt;
        this.matKurzzeichen = matKurzzeichen;
        this.mz3 = mz3;
    }

    public Integer getRowID() {
        return rowID;
    }

    public Timestamp getRowTimestamp() {
        return rowTimestamp;
    }

    public Integer getRowUserID() {
        return rowUserID;
    }

    public UUID getRowGUID() {
        return rowGUID;
    }

    public Timestamp getRowCreationTimestamp() {
        return rowCreationTimestamp;
    }

    public Date getRowEarliestDelDate() {
        return rowEarliestDelDate;
    }

    public Date getRowLatestDelDate() {
        return rowLatestDelDate;
    }

    public Double getPlattenID() {
        return plattenID;
    }

    public String getLagerPlatz() {
        return lagerPlatz;
    }

    public Double getLng() {
        return lng;
    }

    public Double getBrt() {
        return brt;
    }

    public String getMatKurzzeichen() {
        return matKurzzeichen;
    }

    public String getMz3() {
        return mz3;
    }

    public Double getOptimiert() {
        return optimiert;
    }

    public Double getProduktion() {
        return produktion;
    }

    public String getAuslagerId() {
        return mz6;
    }

    public String getAuslagerInfo() {
        return mz1;
    }

    public String getAuslagerDatum() {
        return mzd1;
    }

    public Double getMenge() {
        return menge;
    }

    public String getEinlagerDatum() {
        return mzd2;
    }

    public void setLagerPlatz(String lagerPlatz) {
        this.lagerPlatz = lagerPlatz;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public void setBrt(Double brt) {
        this.brt = brt;
    }

    public void setMatKurzzeichen(String matKurzzeichen) {
        this.matKurzzeichen = matKurzzeichen;
    }

}
