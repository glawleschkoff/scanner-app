package de.glawleschkoff.scannerapp.old;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.glawleschkoff.scannerapp.Repository;

public class MainViewModel extends ViewModel {

    private Repository repository;
    private OptionRepository optionRepository;
    private MutableLiveData<List<RecyclerViewTopItem>> topItems;
    private MutableLiveData<List<RecyclerViewBottomItem>> bottomItems;

    public MainViewModel() {
        repository = new Repository();
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