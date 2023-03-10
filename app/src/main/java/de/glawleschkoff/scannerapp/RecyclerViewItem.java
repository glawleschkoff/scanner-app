package de.glawleschkoff.scannerapp;

public class RecyclerViewItem {
    private String leftText;
    private String rightText;

    public RecyclerViewItem(String leftText, String rightText){
        this.leftText = leftText;
        this.rightText = rightText;
    }

    public String getLeftText(){
        return leftText;
    }
    public String getRightText(){
        return rightText;
    }
}
