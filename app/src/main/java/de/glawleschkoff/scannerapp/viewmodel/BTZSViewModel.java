package de.glawleschkoff.scannerapp.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.glawleschkoff.scannerapp.model.BauteilModel;
import de.glawleschkoff.scannerapp.model.BTZSFeedbackModel;
import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.remote.Repository;

public class BTZSViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<ResponseWrapper<BauteilModel>> responseBauteil;
    private final MutableLiveData<ResponseWrapper<String>> responseFeedback;
    private final MutableLiveData<ResponseWrapper<Bitmap>> responseBitmap;

    public BTZSViewModel(){
        repository = Repository.getInstance();
        responseBauteil = new MutableLiveData<>();
        responseFeedback = new MutableLiveData<>();
        responseBitmap = new MutableLiveData<>();
    }

    public void requestBauteil(String id){
        repository.requestBauteil(id, responseBauteil);
    }
    public void createFeedback(BTZSFeedbackModel BTZSFeedbackModel){
        repository.createFeedback(BTZSFeedbackModel.toCsvName(), BTZSFeedbackModel.toCsv());
    }
    public void requestFeedback(String name){
        repository.requestFeedback(name, responseFeedback);
    }
    public void requestBitmap(String id, String name){
        repository.requestBitmap(id, name, responseBitmap);
    }

    public LiveData<ResponseWrapper<BauteilModel>> getResponseBauteil(){
        return responseBauteil;
    }
    public LiveData<ResponseWrapper<String>> getResponseFeedback(){
        return responseFeedback;
    }
    public LiveData<ResponseWrapper<Bitmap>> getResponseBitmap(){
        return responseBitmap;
    }
}