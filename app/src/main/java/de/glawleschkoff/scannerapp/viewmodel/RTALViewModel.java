package de.glawleschkoff.scannerapp.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import de.glawleschkoff.scannerapp.model.LagerModel;
import de.glawleschkoff.scannerapp.model.USERPlattenlagerModel;
import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.remote.Repository;

public class RTALViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<ResponseWrapper<USERPlattenlagerModel>> responseUSERPlattenlager;
    private final MutableLiveData<String> tempLagerplatz;
    private final MutableLiveData<ResponseWrapper<LagerModel>> responseLager;
    private final MutableLiveData<ResponseWrapper<Bitmap>> responseBitmap;


    public RTALViewModel(){
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

    public void updateUSERPlattenlager(String mitarbeiter) {
        repository.updateUSERPlattenlager(
                responseUSERPlattenlager.getValue().getResponse().getPlattenID(),
                responseUSERPlattenlager.getValue().getResponse().getLagerPlatz(),
                responseUSERPlattenlager.getValue().getResponse().getLng(),
                responseUSERPlattenlager.getValue().getResponse().getBrt(),
                responseUSERPlattenlager.getValue().getResponse().getMz3(),
                "Entnahme",
                mitarbeiter,
                new Date(System.currentTimeMillis()),
                0.0);
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

    public void setUSERPlattenlagerModel(USERPlattenlagerModel USERPlattenlagerModel){
        responseUSERPlattenlager.setValue(new ResponseWrapper<>(USERPlattenlagerModel, responseUSERPlattenlager.getValue().getErrorMessage()));
    }
    public void setTempLagerplatz(String tempLagerplatz) {
        this.tempLagerplatz.setValue(tempLagerplatz);
    }
}
