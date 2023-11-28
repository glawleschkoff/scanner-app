package de.glawleschkoff.scannerapp.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.glawleschkoff.scannerapp.model.BauteilModel;
import de.glawleschkoff.scannerapp.model.KommWagenModel;
import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.remote.Repository;

public class PRQMViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<ResponseWrapper<BauteilModel>> responseBauteil;
    private final MutableLiveData<ResponseWrapper<KommWagenModel>> responseKommWagen;
    private final MutableLiveData<ResponseWrapper<Bitmap>> responseBitmap;

    public PRQMViewModel(){
        repository = Repository.getInstance();
        responseBauteil = new MutableLiveData<>(new ResponseWrapper<>());
        responseKommWagen = new MutableLiveData<>(new ResponseWrapper<>());
        responseBitmap = new MutableLiveData<>(new ResponseWrapper<>());
    }

    public void requestBauteil(String id){
        repository.requestBauteil(id, responseBauteil);
    }
    public void requestKommWagen(String auftrag){
        repository.requestKommWagen(auftrag, responseKommWagen);
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
    public LiveData<ResponseWrapper<KommWagenModel>> getResponseKommWagen() {
        return responseKommWagen;
    }
    public LiveData<ResponseWrapper<Bitmap>> getResponseBitmap(){
        return responseBitmap;
    }
}
