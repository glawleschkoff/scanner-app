package de.glawleschkoff.scannerapp.viewmodel;


import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.model.BauteilLogModel;
import de.glawleschkoff.scannerapp.model.BauteilModel;
import de.glawleschkoff.scannerapp.model.CNCFeedbackModel;
import de.glawleschkoff.scannerapp.model.FeedbackModel;
import de.glawleschkoff.scannerapp.model.KntFeedbackModel;
import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.remote.Repository;

public class InfoViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<ResponseWrapper<BauteilModel>> responseBauteil;
    private final MutableLiveData<ResponseWrapper<FeedbackModel>> responseFeedback;
    private final MutableLiveData<ResponseWrapper<Bitmap>> responseBitmap;
    private final MutableLiveData<ResponseWrapper<List<CNCFeedbackModel>>> responseCNCFeedback;
    private final MutableLiveData<ResponseWrapper<List<KntFeedbackModel>>> responseKntFeedback;
    private final MutableLiveData<ResponseWrapper<List<BauteilLogModel>>> responseBauteilLog;

    public InfoViewModel(){
        repository = Repository.getInstance();
        responseBauteil = new MutableLiveData<>();
        responseFeedback = new MutableLiveData<>();
        responseBitmap = new MutableLiveData<>();
        responseCNCFeedback = new MutableLiveData<>();
        responseKntFeedback = new MutableLiveData<>();
        responseBauteilLog = new MutableLiveData<>();
    }

    public void requestBauteil(String id){
        repository.requestBauteil(id, responseBauteil);
    }
    public void requestFeedback(String name){
        repository.requestFeedback(name, responseFeedback);
    }
    public void requestBitmap(String id, String name){
        repository.requestBitmap(id, name, responseBitmap);
    }
    public void requestCNCFeedback(String id){
        repository.requestCNCFeedback(id, responseCNCFeedback);
    }
    public void requestKntFeedback(String id){
        repository.requestKntFeedback(id, responseKntFeedback);
    }
    public void requestBauteilLog(String id){
        repository.requestBauteilLog(id, responseBauteilLog);
    }

    public LiveData<ResponseWrapper<BauteilModel>> getResponseBauteil(){
        return responseBauteil;
    }
    public LiveData<ResponseWrapper<FeedbackModel>> getResponseFeedback(){
        return responseFeedback;
    }
    public LiveData<ResponseWrapper<Bitmap>> getResponseBitmap(){
        return responseBitmap;
    }
    public LiveData<ResponseWrapper<List<CNCFeedbackModel>>> getResponseCNCFeedback(){
        return responseCNCFeedback;
    }
    public LiveData<ResponseWrapper<List<KntFeedbackModel>>> getResponseKntFeedback(){
        return responseKntFeedback;
    }
    public LiveData<ResponseWrapper<List<BauteilLogModel>>> getResponseBauteilLog(){
        return responseBauteilLog;
    }
}
