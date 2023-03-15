package de.glawleschkoff.scannerapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.List;

public class InfoViewModel extends ViewModel {

    private Repository repository;
    private MutableLiveData<List<RecyclerViewItem>> data;
    private MutableLiveData<Integer> responseState;

    public InfoViewModel(){
        repository = new Repository();
        data = repository.bindData();
        responseState = repository.bindResponseSuccessfull();
    }

    public void getBauteil(String id){
        repository.getBauteil(id);
    }
    public void createFeedback(FeedbackModel feedbackModel){
        repository.createFeedback(feedbackModel);
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