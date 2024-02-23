package de.glawleschkoff.scannerapp.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.model.LagerModel;
import de.glawleschkoff.scannerapp.model.PlattenlagerModel;
import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.remote.Repository;

public class PLEBViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<ResponseWrapper<PlattenlagerModel>> responsePlattenlager;
    private final MutableLiveData<String> tempLagerplatz;
    private final MutableLiveData<ResponseWrapper<LagerModel>> responseLager;
    private final MutableLiveData<ResponseWrapper<Bitmap>> responseBitmap;


    public PLEBViewModel(){
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
        repository.requestBitmapMaterial(name, responseBitmap);
    }

    public void updatePlattenlager() {
        repository.updatePlattenlagerBearbeiten(
                responsePlattenlager.getValue().getResponse().getPlattenID(),
                responsePlattenlager.getValue().getResponse().getLagerPlatz(),
                responsePlattenlager.getValue().getResponse().getLng(),
                responsePlattenlager.getValue().getResponse().getBrt(),
                responsePlattenlager.getValue().getResponse().getMz3(),
                responsePlattenlager.getValue().getResponse().getAuslagerId(),
                responsePlattenlager.getValue().getResponse().getAuslagerInfo(),
                responsePlattenlager.getValue().getResponse().getAuslagerDatum(),
                responsePlattenlager.getValue().getResponse().getMenge());
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
