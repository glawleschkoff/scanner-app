package de.glawleschkoff.scannerapp.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardRVItem {

    private List<RVItem> fixedList;
    private List<RVItem> hiddenList;

    public CardRVItem(List<RVItem> fixedList, List<RVItem> hiddenList){
        this.fixedList = fixedList;
        this.hiddenList = hiddenList;
    }

    public List<RVItem> getFixedList() {
        return fixedList;
    }

    public List<RVItem> getHiddenList() {
        return hiddenList;
    }
}
