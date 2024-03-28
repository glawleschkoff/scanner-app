package de.glawleschkoff.scannerapp.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.glawleschkoff.scannerapp.model.USERALBDetailsModel;
//import de.glawleschkoff.scannerapp.model.BTZSFeedbackModel;
import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.remote.Repository;

public class BTZSViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<ResponseWrapper<USERALBDetailsModel>> responseUSERALBDetails;
    private final MutableLiveData<ResponseWrapper<Bitmap>> responseBitmap;

    public BTZSViewModel(){
        repository = Repository.getInstance();
        responseUSERALBDetails = new MutableLiveData<>(new ResponseWrapper<>());
        responseBitmap = new MutableLiveData<>(new ResponseWrapper<>());
    }

    public void requestUSERALBDetails(String id){
        repository.requestUSERALBDetails(id, responseUSERALBDetails);
    }

    public void requestBitmap(String id, String name){
        repository.requestBitmapBauteil(id, name, responseBitmap);
    }

    public void updateUSERALBDetails(String exemplarNr, String scannerAntwort) {
        repository.updateUSERALBDetails(exemplarNr, scannerAntwort);
    }

    public LiveData<ResponseWrapper<USERALBDetailsModel>> getResponseUSERALBDetails(){
        return responseUSERALBDetails;
    }
    public LiveData<ResponseWrapper<Bitmap>> getResponseBitmap(){
        return responseBitmap;
    }
}