package de.glawleschkoff.scannerapp.util;

public class RVItem {

    private String name;
    private String wert;

    public RVItem(String name, String wert){
        this.name = name;
        this.wert = wert;
    }

    public String getName(){
        return name;
    }
    public String getWert(){
        return wert;
    }
}
