package de.glawleschkoff.scannerapp.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.glawleschkoff.scannerapp.model.BauteilModel;
import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.remote.Repository;

public class BTEDViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<ResponseWrapper<BauteilModel>> responseBauteil;
    private final MutableLiveData<ResponseWrapper<Bitmap>> responseBitmap;

    public BTEDViewModel(){
        repository = Repository.getInstance();
        responseBauteil = new MutableLiveData<>(new ResponseWrapper<>());
        responseBitmap = new MutableLiveData<>(new ResponseWrapper<>());
    }

    public void requestBauteil(String id){
        repository.requestBauteil(id, responseBauteil);
    }
    public void requestBitmap(String id, String name){
        repository.requestBitmapBauteil(id, name, responseBitmap);
    }

    public void updateBauteil(String exemplarNr, String scannerAntwort) {
        repository.updateBauteil(exemplarNr, scannerAntwort);
    }

    public LiveData<ResponseWrapper<BauteilModel>> getResponseBauteil(){
        return responseBauteil;
    }
    public LiveData<ResponseWrapper<Bitmap>> getResponseBitmap(){
        return responseBitmap;
    }
}
