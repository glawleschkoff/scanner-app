package de.glawleschkoff.scannerapp.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.glawleschkoff.scannerapp.model.LagerModel;
import de.glawleschkoff.scannerapp.model.USERPlattenlagerModel;
import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.remote.Repository;

public class RTBAViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<ResponseWrapper<USERPlattenlagerModel>> responseUSERPlattenlager;
    private final MutableLiveData<String> tempLagerplatz;
    private final MutableLiveData<ResponseWrapper<LagerModel>> responseLager;
    private final MutableLiveData<ResponseWrapper<Bitmap>> responseBitmap;


    public RTBAViewModel(){
        repository = Repository.getInstance();
        responseUSERPlattenlager = new MutableLiveData<>(new ResponseWrapper<>());
        tempLagerplatz = new MutableLiveData<>();
        responseLager = new MutableLiveData<>(new ResponseWrapper<>());
        responseBitmap = new MutableLiveData<>(new ResponseWrapper<>());
    }

    public void requestUSERPlattenlager(String id) {
        repository.requestUSERPlattenlager(id, responseUSERPlattenlager);
    }
    public void requestLager(String id) {
        repository.requestLager(id, responseLager);
    }
    public void requestBitmap(String name){
        repository.requestBitmapMaterial(name, responseBitmap);
    }

    public void updateUSERPlattenlager() {
        repository.updateUSERPlattenlagerBearbeiten(
                responseUSERPlattenlager.getValue().getResponse().getPlattenID(),
                responseUSERPlattenlager.getValue().getResponse().getLagerPlatz(),
                responseUSERPlattenlager.getValue().getResponse().getLng(),
                responseUSERPlattenlager.getValue().getResponse().getBrt(),
                responseUSERPlattenlager.getValue().getResponse().getMz3(),
                responseUSERPlattenlager.getValue().getResponse().getAuslagerId(),
                responseUSERPlattenlager.getValue().getResponse().getAuslagerInfo(),
                responseUSERPlattenlager.getValue().getResponse().getAuslagerDatum(),
                responseUSERPlattenlager.getValue().getResponse().getMenge());
    }

    public LiveData<ResponseWrapper<USERPlattenlagerModel>> getUSERPlattenlager() {
        return responseUSERPlattenlager;
    }
    public LiveData<String> getTempLagerplatz() {
        return tempLagerplatz;
    }
    public LiveData<ResponseWrapper<LagerModel>> getLager() {
        return responseLager;
    }
    public LiveData<ResponseWrapper<Bitmap>> getResponseBitmap(){
        return responseBitmap;
    }

    public void setUSERPlattenlager(USERPlattenlagerModel USERPlattenlagerModel){
        responseUSERPlattenlager.setValue(new ResponseWrapper<>(USERPlattenlagerModel, responseUSERPlattenlager.getValue().getErrorMessage()));
    }
    public void setTempLagerplatz(String tempLagerplatz) {
        this.tempLagerplatz.setValue(tempLagerplatz);
    }
}
