package de.glawleschkoff.scannerapp.old;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BauteilModel {
    private Integer rowID;
    private Timestamp rowTimestamp;
    private Integer rowUserID;
    private String rowDDMFields;
    private UUID rowGUID;
    private Timestamp rowCreationTimestamp;
    private Date rowEarliestDelDate;
    private Date rowLatestDelDate;
    private String exemplarNr;
    private String albauftrag;
    private String albposition;
    private String kundenAuftrag;
    private String kundenPosition;
    private String scannerAnweisung;
    private String scannerAntwort;
    private String originalIDNUM;
    private String platte;
    private String ka;
    private String kb;
    private String kc;
    private String kd;
    private String ke;
    private String kf;
    private String kg;
    private String kh;
    private String status;
    private String kommentar;
    private String prodFreigabe;
    private String f20;

    static public Map<String, String> object2Map(Object o)
    {
        Class co = o.getClass();
        Field[] cfields = co.getDeclaredFields();
        Map<String, String> ret = new HashMap<>();
        for(Field f: cfields)
        {
            String attributeName = f.getName();
            String getterMethodName = "get"
                    + attributeName.substring(0, 1).toUpperCase()
                    + attributeName.substring(1, attributeName.length());
            Method m = null;
            try {
                m = co.getMethod(getterMethodName);
                Object valObject = m.invoke(o);
                if(attributeName == "rowDDMFields"){
                    ret.putAll(ddmToMap(valObject.toString()));
                } else {
                    if(valObject == null){
                        ret.put(attributeName, "");
                    } else {
                        ret.put(attributeName, valObject.toString());
                    }
                }

            } catch (Exception e) {
                continue;
            }
        }
        return ret;
    }
    private static Map<String, String> ddmToMap(String s){
        Map<String, String> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(s);
        while( matcher.find() ) {
            list.add(matcher.group(1));
            System.out.println( matcher.group(1) );
        }
        for(String str : list){
            map.put(str.split("\\:")[0], str.split("\\:")[1]);
        }


        return map;
    }

    public BauteilModel(String ddm){
        rowDDMFields = ddm;
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

    public String getRowDDMFields() {
        return rowDDMFields;
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

    public String getAlbauftrag() {
        return albauftrag;
    }

    public String getAlbposition() {
        return albposition;
    }

    public String getKundenAuftrag() {
        return kundenAuftrag;
    }

    public String getKundenPosition() {
        return kundenPosition;
    }

    public String getScannerAnweisung() {
        return scannerAnweisung;
    }

    public String getScannerAntwort() {
        return scannerAntwort;
    }

    public String getOriginalIDNUM() {
        return originalIDNUM;
    }

    public String getPlatte() {
        return platte;
    }

    public String getKa() {
        return ka;
    }

    public String getKb() {
        return kb;
    }

    public String getKc() {
        return kc;
    }

    public String getKd() {
        return kd;
    }

    public String getKe() {
        return ke;
    }

    public String getKf() {
        return kf;
    }

    public String getKg() {
        return kg;
    }

    public String getKh() {
        return kh;
    }

    public String getStatus() {
        return status;
    }

    public String getKommentar() {
        return kommentar;
    }

    public String getProdFreigabe() {
        return prodFreigabe;
    }

    public String getF20() {
        return f20;
    }
}
