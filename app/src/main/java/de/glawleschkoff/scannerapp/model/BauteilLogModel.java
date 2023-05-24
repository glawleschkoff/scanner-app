package de.glawleschkoff.scannerapp.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;

public class BauteilLogModel {

    private Integer rowID;
    private Timestamp rowTimestamp;
    private Integer rowUserID;
    private UUID rowGUID;
    private Timestamp rowCreationTimestamp;
    private Date rowEarliestDelDate;
    private Date rowLatestDelDate;
    private String exemplarNr;
    private String datum;
    private String uhrzeit;
    private String job;
    private String vorgang;
    private String protokoll;

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

    public String getDatum() {
        String[] s = datum.split("-");
        if(s.length>=3){
            return s[2]+"."+s[1]+"."+s[0];
        } else {
            return "";
        }

    }

    public String getUhrzeit() {
        String[] s = uhrzeit.split("\\.");
        System.out.println(Arrays.toString(s));
        if(s.length==2){
            if(s[1].length()==1){
                return  s[0]+":00:0"+s[1].substring(0,1);
            } else if(s[1].length()==2){
                return  s[0]+":00:"+s[1].substring(0,2);
            } else if(s[1].length()==3){
                return  s[0]+":0"+s[1].substring(0,1)+":"+s[1].substring(1,3);
            } else {
                return  s[0]+":"+s[1].substring(0,2)+":"+s[1].substring(2,4);
            }
        } else {
            return uhrzeit;
        }
    }

    public String getJob() {
        return job;
    }

    public String getVorgang() {
        return vorgang;
    }

    public String getProtokoll() {
        return protokoll;
    }
}
