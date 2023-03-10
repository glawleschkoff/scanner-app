package de.glawleschkoff.scannerapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.glawleschkoff.scannerapp.old.BauteilModel;
import de.glawleschkoff.scannerapp.old.BauteilRepository;

public class ResetViewModel extends ViewModel {

    private BauteilRepository bauteilRepository;
    private MutableLiveData<List<RecyclerViewItem>> data;
    private MutableLiveData<Boolean> responseSuccessful;

    public ResetViewModel(){
        bauteilRepository = new BauteilRepository();
        data = bauteilRepository.bindData();
        responseSuccessful = bauteilRepository.bindResponseSuccessfull();
    }

    public void requestData(String id){
        bauteilRepository.getData(id);
    }
    public LiveData<List<RecyclerViewItem>> data(){
        return data;
    }
    public LiveData<Boolean> responseSuccessful(){
        return responseSuccessful;
    }
}