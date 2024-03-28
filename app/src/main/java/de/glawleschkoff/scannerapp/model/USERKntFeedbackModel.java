package de.glawleschkoff.scannerapp.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

public class USERKntFeedbackModel {

    private Integer rowID;
    private Timestamp rowTimestamp;
    private Integer rowUserID;
    private UUID rowGUID;
    private Timestamp rowCreationTimestamp;
    private Date rowEarliestDelDate;
    private Date rowLatestDelDate;
    private String exemplarNr;
    private String dateiName;
    private String laufNr;
    private String kante;
    private String platteLng;
    private String platteBrt;
    private String platteDck;
    private String kantenMat;
    private String kantenLng;
    private String datum;
    private String uhrzeit;

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

    public String getExemplarNr() {
        return exemplarNr;
    }

    public String getDateiName() {
        return dateiName;
    }

    public String getLaufNr() {
        return laufNr;
    }

    public String getKante() {
        return kante;
    }

    public String getPlatteLng() {
        return platteLng;
    }

    public String getPlatteBrt() {
        return platteBrt;
    }

    public String getPlatteDck() {
        return platteDck;
    }

    public String getKantenMat() {
        return kantenMat;
    }

    public String getKantenLng() {
        return kantenLng;
    }

    public String getDatum() {
        String[] s = datum.split("-");
        if(s.length>=3){
            return s[2]+"."+s[1]+"."+s[0];
        } else {
            return "";
        }
    }

    public String getUhrzeit() {
        return uhrzeit;
    }
}
