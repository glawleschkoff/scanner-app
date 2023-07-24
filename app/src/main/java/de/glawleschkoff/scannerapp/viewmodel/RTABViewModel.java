package de.glawleschkoff.scannerapp.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.model.LagerModel;
import de.glawleschkoff.scannerapp.model.PlattenlagerModel;
import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.remote.Repository;

public class RTABViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<ResponseWrapper<PlattenlagerModel>> responsePlattenlager;
    private final MutableLiveData<String> tempLagerplatz;
    private final MutableLiveData<ResponseWrapper<LagerModel>> responseLager;
    private final MutableLiveData<ResponseWrapper<Bitmap>> responseBitmap;


    public RTABViewModel(){
        repository = Repository.getInstance();
        responsePlattenlager = new MutableLiveData<>(new ResponseWrapper<>());
        tempLagerplatz = new MutableLiveData<>();
        responseLager = new MutableLiveData<>(new ResponseWrapper<>());
        responseBitmap = new MutableLiveData<>(new ResponseWrapper<>());
    }

    public void requestPlattenlager(String id) {
        repository.requestPlattenlager(id, responsePlattenlager);
    }
    public void requestLager(String id) {
        repository.requestLager(id, responseLager);
    }
    public void requestBitmap(String name){
        System.out.println(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        System.out.println(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Timestamp(System.currentTimeMillis())));
        repository.requestBitmapMaterial(name, responseBitmap);
    }

    public void updatePlattenlager(String mitarbeiter) {
        repository.updatePlattenlager(
                responsePlattenlager.getValue().getResponse().getPlattenID(),
                responsePlattenlager.getValue().getResponse().getLagerPlatz(),
                responsePlattenlager.getValue().getResponse().getLng(),
                responsePlattenlager.getValue().getResponse().getBrt(),
                responsePlattenlager.getValue().getResponse().getMz3(),
                "Entnahme",
                mitarbeiter+" "+new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Timestamp(System.currentTimeMillis())),
                new Date(System.currentTimeMillis()),
                0.0);
    }

    public LiveData<ResponseWrapper<PlattenlagerModel>> getPlattenlagerModel() {
        return responsePlattenlager;
    }
    public LiveData<String> getTempLagerplatz() {
        return tempLagerplatz;
    }
    public LiveData<ResponseWrapper<LagerModel>> getLagerModel() {
        return responseLager;
    }
    public LiveData<ResponseWrapper<Bitmap>> getResponseBitmap(){
        return responseBitmap;
    }

    public void setPlattenlagerModel(PlattenlagerModel plattenlagerModel){
        responsePlattenlager.setValue(new ResponseWrapper<>(plattenlagerModel, responsePlattenlager.getValue().getErrorMessage()));
    }
    public void setTempLagerplatz(String tempLagerplatz) {
        this.tempLagerplatz.setValue(tempLagerplatz);
    }
}
