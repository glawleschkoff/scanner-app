package de.glawleschkoff.scannerapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;

public class InfoViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<Response<BauteilModel>> responseMutableLiveData;

    public InfoViewModel(){
        repository = new Repository();
        responseMutableLiveData = repository.getResponseMutableLiveData();
    }

    public void getBauteil(String id){
        repository.getBauteil(id);
    }
    public void createFeedback(FeedbackModel feedbackModel){
        repository.createFeedback(feedbackModel);
    }
    public LiveData<Response<BauteilModel>> getResponseLivedata(){
        return responseMutableLiveData;
    }

}