package de.glawleschkoff.scannerapp.util;

import java.util.List;

public class CollapsableCardRVItem {

    private List<RVItem> fixedList;
    private List<RVItem> hiddenList;

    public CollapsableCardRVItem(List<RVItem> fixedList, List<RVItem> hiddenList){
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
