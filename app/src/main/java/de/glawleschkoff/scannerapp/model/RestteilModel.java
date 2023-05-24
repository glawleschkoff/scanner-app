package de.glawleschkoff.scannerapp.model;

public class RestteilModel {

    private String id;
    private String kante;
    private int länge;
    private int breite;

    public RestteilModel(String csv){
        try{
            String[] s = csv.split("%");
            id = s[0];
            kante = s[1];
            länge = Integer.parseInt(s[2]);
            breite = Integer.parseInt(s[3]);
        } catch (Exception e){
            id = null;
        }
    }
    public RestteilModel(String id, String kante, int länge, int breite){
        this.id = id;
        this.kante = kante;
        this.länge = länge;
        this.breite = breite;
    }

    public String getId() {
        return id;
    }

    public String getKante() {
        return kante;
    }

    public int getLänge() {
        return länge;
    }

    public int getBreite() {
        return breite;
    }
}
