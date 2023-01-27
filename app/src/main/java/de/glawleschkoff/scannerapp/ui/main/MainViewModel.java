package de.glawleschkoff.scannerapp.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.glawleschkoff.scannerapp.BauteilRepository;
import de.glawleschkoff.scannerapp.OptionRepository;
import de.glawleschkoff.scannerapp.RecyclerViewBottomItem;
import de.glawleschkoff.scannerapp.RecyclerViewTopItem;

public class MainViewModel extends ViewModel {

    private BauteilRepository bauteilRepository;
    private OptionRepository optionRepository;
    private MutableLiveData<List<RecyclerViewTopItem>> topItems;
    private MutableLiveData<List<RecyclerViewBottomItem>> bottomItems;

    public MainViewModel() {
        bauteilRepository = new BauteilRepository();
        optionRepository = new OptionRepository();
    }

    public LiveData<List<RecyclerViewTopItem>> getTopItems(){
        if(topItems == null){
            topItems = new MutableLiveData<>();
        }
        return topItems;
    }
    public LiveData<List<RecyclerViewBottomItem>> getBottomItems(){
        if(bottomItems == null){
            bottomItems = new MutableLiveData<>();
        }
        return bottomItems;
    }

}