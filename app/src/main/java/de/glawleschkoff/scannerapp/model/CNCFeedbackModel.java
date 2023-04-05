package de.glawleschkoff.scannerapp.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

public class CNCFeedbackModel {

    private Integer rowID;
    private Timestamp rowTimestamp;
    private Integer rowUserID;
    private UUID rowGUID;
    private Timestamp rowCreationTimestamp;
    private Date rowEarliestDelDate;
    private Date rowLatestDelDate;
    private String exemplarNr;
    private String fortschritt;
    private String maschine;
    private String datum;
    private String uhrzeit;
    private String bestandsBuchung;

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

    public String getFortschritt() {
        return fortschritt;
    }

    public String getMaschine() {
        return maschine;
    }

    public String getDatum() {
        String[] s = datum.split("-");
        return s[2]+"."+s[1]+"."+s[0];
    }

    public String getUhrzeit() {
        return uhrzeit;
    }

    public String getBestandsBuchung() {
        return bestandsBuchung;
    }
}
