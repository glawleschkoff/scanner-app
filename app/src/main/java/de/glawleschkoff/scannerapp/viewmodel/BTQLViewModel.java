package de.glawleschkoff.scannerapp.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.glawleschkoff.scannerapp.model.USERALBDetailsModel;
import de.glawleschkoff.scannerapp.model.USERKommWagenModel;
import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.remote.Repository;

public class BTQLViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<ResponseWrapper<USERALBDetailsModel>> responseUSERALBDetails;
    private final MutableLiveData<ResponseWrapper<USERKommWagenModel>> responseUSERKommWagen;
    private final MutableLiveData<ResponseWrapper<Bitmap>> responseBitmap;
    private final MutableLiveData<String> wagenCode;

    public BTQLViewModel(){
        repository = Repository.getInstance();
        responseUSERALBDetails = new MutableLiveData<>(new ResponseWrapper<>());
        responseUSERKommWagen = new MutableLiveData<>(new ResponseWrapper<>());
        responseBitmap = new MutableLiveData<>(new ResponseWrapper<>());
        wagenCode = new MutableLiveData<>();
    }

    public void requestUSERALBDetails(String id){
        repository.requestUSERALBDetails(id, responseUSERALBDetails);
    }
    public void requestUSERKommWagen(String auftrag, Integer losNr){
        repository.requestUSERKommWagen(auftrag, losNr, responseUSERKommWagen);
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
    public LiveData<ResponseWrapper<USERKommWagenModel>> getResponseUSERKommWagen() {
        return responseUSERKommWagen;
    }
    public LiveData<ResponseWrapper<Bitmap>> getResponseBitmap(){
        return responseBitmap;
    }
    public LiveData<String> getWagenCode(){
        return wagenCode;
    }
    public void setWagenCode(String s){
        wagenCode.setValue(s);
    }
}
