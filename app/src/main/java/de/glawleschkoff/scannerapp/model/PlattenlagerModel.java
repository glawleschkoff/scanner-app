package de.glawleschkoff.scannerapp.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

public class PlattenlagerModel {
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

    public PlattenlagerModel(Double plattenID, String lagerPlatz, Double lng, Double brt, String matKurzzeichen, String mz3){
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
