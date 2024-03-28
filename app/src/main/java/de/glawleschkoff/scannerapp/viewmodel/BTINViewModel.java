package de.glawleschkoff.scannerapp.viewmodel;


import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.glawleschkoff.scannerapp.model.USERBauteilLogModel;
import de.glawleschkoff.scannerapp.model.USERALBDetailsModel;
import de.glawleschkoff.scannerapp.model.USERCNCFeedbackModel;
import de.glawleschkoff.scannerapp.model.USERKntFeedbackModel;
import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.remote.Repository;

public class BTINViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<ResponseWrapper<USERALBDetailsModel>> responseUSERALBDetails;
    private final MutableLiveData<ResponseWrapper<Bitmap>> responseBitmap;
    private final MutableLiveData<ResponseWrapper<List<USERCNCFeedbackModel>>> responseUSERCNCFeedback;
    private final MutableLiveData<ResponseWrapper<List<USERKntFeedbackModel>>> responseUSERKntFeedback;
    private final MutableLiveData<ResponseWrapper<List<USERBauteilLogModel>>> responseUSERBauteilLog;

    public BTINViewModel(){
        repository = Repository.getInstance();
        responseUSERALBDetails = new MutableLiveData<>(new ResponseWrapper<>());
        responseBitmap = new MutableLiveData<>(new ResponseWrapper<>());
        responseUSERCNCFeedback = new MutableLiveData<>(new ResponseWrapper<>());
        responseUSERKntFeedback = new MutableLiveData<>(new ResponseWrapper<>());
        responseUSERBauteilLog = new MutableLiveData<>(new ResponseWrapper<>());
    }

    public void requestUSERALBDetails(String id){
        repository.requestUSERALBDetails(id, responseUSERALBDetails);
    }
    public void requestBitmap(String id, String name){
        repository.requestBitmapBauteil(id, name, responseBitmap);
    }
    public void requestUSERCNCFeedback(String id){
        repository.requestUSERCNCFeedback(id, responseUSERCNCFeedback);
    }
    public void requestUSERKntFeedback(String id){
        repository.requestUSERKntFeedback(id, responseUSERKntFeedback);
    }
    public void requestUSERBauteilLog(String id){
        repository.requestUSERBauteilLog(id, responseUSERBauteilLog);
    }

    public LiveData<ResponseWrapper<USERALBDetailsModel>> getResponseUSERALBDetails(){
        return responseUSERALBDetails;
    }
    public LiveData<ResponseWrapper<Bitmap>> getResponseBitmap(){
        return responseBitmap;
    }
    public LiveData<ResponseWrapper<List<USERCNCFeedbackModel>>> getResponseUSERCNCFeedback(){
        return responseUSERCNCFeedback;
    }
    public LiveData<ResponseWrapper<List<USERKntFeedbackModel>>> getResponseUSERKntFeedback(){
        return responseUSERKntFeedback;
    }
    public LiveData<ResponseWrapper<List<USERBauteilLogModel>>> getResponseUSERBauteilLog(){
        return responseUSERBauteilLog;
    }
}
