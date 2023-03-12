package de.glawleschkoff.scannerapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.glawleschkoff.scannerapp.old.BauteilRepository;

public class InfoViewModel extends ViewModel {

    private BauteilRepository bauteilRepository;
    private MutableLiveData<List<RecyclerViewItem>> data;
    private MutableLiveData<Integer> responseState;

    public InfoViewModel(){
        bauteilRepository = new BauteilRepository();
        data = bauteilRepository.bindData();
        responseState = bauteilRepository.bindResponseSuccessfull();
    }

    public void requestData(String id){
        bauteilRepository.getData(id);
    }
    public LiveData<List<RecyclerViewItem>> data(){
        return data;
    }
    public LiveData<Integer> responseSuccessful(){
        return responseState;
    }
    public void resetResponseState(){
        responseState.setValue(0);
    }
    public void resetData(){
        data.setValue(Collections.singletonList(new RecyclerViewItem("Typ...", "Wert...")));
    }

}