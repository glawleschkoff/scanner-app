package de.glawleschkoff.scannerapp.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.model.LagerModel;
import de.glawleschkoff.scannerapp.model.PlattenlagerModel;
import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.remote.Repository;

public class RTHFViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<PlattenlagerModel> plattenlager;
    private final MutableLiveData<String> tempLagerplatz;
    private final MutableLiveData<ResponseWrapper<LagerModel>> responseLager;
    private final MutableLiveData<ResponseWrapper<Bitmap>> responseBitmap;
    private final MutableLiveData<ResponseWrapper<Double>> maxPlattenID;
    private final MutableLiveData<ResponseWrapper<List<String>>> responseMaterialien;


    public RTHFViewModel(){
        repository = Repository.getInstance();
        plattenlager = new MutableLiveData<>();
        tempLagerplatz = new MutableLiveData<>();
        responseLager = new MutableLiveData<>(new ResponseWrapper<>());
        responseBitmap = new MutableLiveData<>(new ResponseWrapper<>());
        maxPlattenID = new MutableLiveData<>(new ResponseWrapper<>());
        responseMaterialien = new MutableLiveData<>(new ResponseWrapper<>());
    }

    public void requestLager(String id) {
        repository.requestLager(id, responseLager);
    }
    public void requestBitmap(String name){
        repository.requestBitmapMaterial(name, responseBitmap);
    }
    public void requestMaxPlattenID() {
        repository.getMaxPlattenID(maxPlattenID);
    }
    public void requestMaterialien() {
        repository.requestMaterialien(responseMaterialien);
    }

    public void insertPlattenlager(Integer rowUserId, String matKurzzeichen, Double plattenId, String lagerplatz, String mz3, Double lng, Double brt) {
        repository.insertPlattenlager(rowUserId,matKurzzeichen,plattenId,lagerplatz,mz3,lng,brt);
    }

    public LiveData<PlattenlagerModel> getPlattenlagerModel() {
        return plattenlager;
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
    public LiveData<ResponseWrapper<Double>> getMaxPlattenID() {
        return maxPlattenID;
    }
    public LiveData<ResponseWrapper<List<String>>> getMaterialien() {
        return responseMaterialien;
    }

    public void setPlattenlagerModel(PlattenlagerModel plattenlagerModel){
        plattenlager.setValue(plattenlagerModel);
    }
    public void setTempLagerplatz(String tempLagerplatz) {
        this.tempLagerplatz.setValue(tempLagerplatz);
    }
    public void setBitmap(Bitmap bitmap) {
        responseBitmap.setValue(new ResponseWrapper<>(bitmap, responseBitmap.getValue().getErrorMessage()));
        System.out.println("reseted bitmap");
    }
}
