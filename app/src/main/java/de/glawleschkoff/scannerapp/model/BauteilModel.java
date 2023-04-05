package de.glawleschkoff.scannerapp.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BauteilModel {
    private Integer rowID;
    private Timestamp rowTimestamp;
    private Integer rowUserID;
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

    private String prodDatum;
    private String fertigDatum;
    private String letztesReset;
    private String beginnPlatte;
    private String beginnKante;
    private String beginnBankQM;
    private String beginnVerpk;
    private String sollZtPlatte;
    private String sollZtKante;
    private String sollZtBankQM;
    private String bauteilAnzGes;
    private String prodStopp;
    private String pl_FLng;
    private String pl_FBrt;
    private String zlng;
    private String zbrt;
    private String prio1Datum;
    private String prio2Datum;
    private String prio;
    private String ardisJob;
    private String ardisSPln;
    private String plattenID;
    private String maschine;
    private String säge;
    private String optiQuote;
    private String optiQtEff;
    private String stripNo;
    private String platteKlein;
    private String ausw;
    private String auslagerID;
    private String ausgelagert;
    private String splan_gedruckt;
    private String bteti_gedruckt;
    private String steti_gedruckt;
    private String baz_Vorgabe;
    private String baz_Fortschritt;
    private String fertigZuschnitt;
    private String zuschnittDatum;
    private String ka_Ist_L;
    private String kb_Ist_L;
    private String kc_Ist_L;
    private String kd_Ist_L;
    private String ke_Ist_L;
    private String kf_Ist_L;
    private String kg_Ist_L;
    private String kh_Ist_L;
    private String ka_Läufe;
    private String kb_Läufe;
    private String kc_Läufe;
    private String kd_Läufe;
    private String ke_Läufe;
    private String kf_Läufe;
    private String kg_Läufe;
    private String kh_Läufe;
    private String ka_Verbrauch;
    private String kb_Verbrauch;
    private String kc_Verbrauch;
    private String kd_Verbrauch;
    private String ke_Verbrauch;
    private String kf_Verbrauch;
    private String kg_Verbrauch;
    private String kh_Verbrauch;
    private String fertigKante;
    private String cp_ausblenden;


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

    public String getProdDatum() {
        return prodDatum;
    }

    public String getFertigDatum() {
        return fertigDatum;
    }

    public String getLetztesReset() {
        return letztesReset;
    }

    public String getBeginnPlatte() {
        return beginnPlatte;
    }

    public String getBeginnKante() {
        return beginnKante;
    }

    public String getBeginnBankQM() {
        return beginnBankQM;
    }

    public String getBeginnVerpk() {
        return beginnVerpk;
    }

    public String getSollZtPlatte() {
        return sollZtPlatte;
    }

    public String getSollZtKante() {
        return sollZtKante;
    }

    public String getSollZtBankQM() {
        return sollZtBankQM;
    }

    public String getBauteilAnzGes() {
        return bauteilAnzGes;
    }

    public String getProdStopp() {
        return prodStopp;
    }

    public String getPl_FLng() {
        return pl_FLng;
    }

    public String getPl_FBrt() {
        return pl_FBrt;
    }

    public String getZlng() {
        return zlng;
    }

    public String getZbrt() {
        return zbrt;
    }

    public String getPrio1Datum() {
        return prio1Datum;
    }

    public String getPrio2Datum() {
        return prio2Datum;
    }

    public String getPrio() {
        return prio;
    }

    public String getArdisJob() {
        return ardisJob;
    }

    public String getArdisSPln() {
        return ardisSPln;
    }

    public String getPlattenID() {
        return plattenID;
    }

    public String getMaschine() {
        return maschine;
    }

    public String getSäge() {
        return säge;
    }

    public String getOptiQuote() {
        return optiQuote;
    }

    public String getOptiQtEff() {
        return optiQtEff;
    }

    public String getStripNo() {
        return stripNo;
    }

    public String getPlatteKlein() {
        return platteKlein;
    }

    public String getAusw() {
        return ausw;
    }

    public String getAuslagerID() {
        return auslagerID;
    }

    public String getAusgelagert() {
        return ausgelagert;
    }

    public String getSplan_gedruckt() {
        return splan_gedruckt;
    }

    public String getBteti_gedruckt() {
        return bteti_gedruckt;
    }

    public String getSteti_gedruckt() {
        return steti_gedruckt;
    }

    public String getBaz_Vorgabe() {
        return baz_Vorgabe;
    }

    public String getBaz_Fortschritt() {
        return baz_Fortschritt;
    }

    public String getFertigZuschnitt() {
        return fertigZuschnitt;
    }

    public String getZuschnittDatum() {
        return zuschnittDatum;
    }

    public String getKa_Ist_L() {
        return ka_Ist_L;
    }

    public String getKb_Ist_L() {
        return kb_Ist_L;
    }

    public String getKc_Ist_L() {
        return kc_Ist_L;
    }

    public String getKd_Ist_L() {
        return kd_Ist_L;
    }

    public String getKe_Ist_L() {
        return ke_Ist_L;
    }

    public String getKf_Ist_L() {
        return kf_Ist_L;
    }

    public String getKg_Ist_L() {
        return kg_Ist_L;
    }

    public String getKh_Ist_L() {
        return kh_Ist_L;
    }

    public String getKa_Läufe() {
        return ka_Läufe;
    }

    public String getKb_Läufe() {
        return kb_Läufe;
    }

    public String getKc_Läufe() {
        return kc_Läufe;
    }

    public String getKd_Läufe() {
        return kd_Läufe;
    }

    public String getKe_Läufe() {
        return ke_Läufe;
    }

    public String getKf_Läufe() {
        return kf_Läufe;
    }

    public String getKg_Läufe() {
        return kg_Läufe;
    }

    public String getKh_Läufe() {
        return kh_Läufe;
    }

    public String getKa_Verbrauch() {
        return ka_Verbrauch;
    }

    public String getKb_Verbrauch() {
        return kb_Verbrauch;
    }

    public String getKc_Verbrauch() {
        return kc_Verbrauch;
    }

    public String getKd_Verbrauch() {
        return kd_Verbrauch;
    }

    public String getKe_Verbrauch() {
        return ke_Verbrauch;
    }

    public String getKf_Verbrauch() {
        return kf_Verbrauch;
    }

    public String getKg_Verbrauch() {
        return kg_Verbrauch;
    }

    public String getKh_Verbrauch() {
        return kh_Verbrauch;
    }

    public String getFertigKante() {
        return fertigKante;
    }

    public String getCp_ausblenden() {
        return cp_ausblenden;
    }
}
