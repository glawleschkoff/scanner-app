package de.glawleschkoff.scannerapp.viewmodel;


import android.os.CountDownTimer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.remote.Repository;


public class MetaViewModel extends ViewModel {

    private final MutableLiveData<String> mitarbeiter;
    private final MutableLiveData<String> scannerNr;
    private final MutableLiveData<Boolean> fiveMinutes;
    private final MutableLiveData<CountDownTimer> timer;
    private final Repository repository;
    private final MutableLiveData<String> prqmLetzterAuftrag;

    public MetaViewModel(){
        repository = Repository.getInstance();
        mitarbeiter = new MutableLiveData<>();
        scannerNr = new MutableLiveData<>("001");
        fiveMinutes = new MutableLiveData<>(true);
        timer = new MutableLiveData<>();
        prqmLetzterAuftrag = new MutableLiveData<>("");
    }

    public LiveData<String> getMitarbeiter(){
        return mitarbeiter;
    }
    public LiveData<String> getScannerNr() {
        return scannerNr;
    }
    public LiveData<Boolean> getFiveMinutes() {
        return fiveMinutes;
    }
    public LiveData<CountDownTimer> getTimer() {
        return timer;
    }
    public LiveData<String> getPrqmLetzterAuftrag(){
        return prqmLetzterAuftrag;
    }

    public void setPrqmLetzterAuftrag(String auftrag){
        this.prqmLetzterAuftrag.setValue(auftrag);
    }

    public void setTimer(long time) {
        timer.setValue(new CountDownTimer(time,time) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mitarbeiter.setValue(null);
            }
        });
    };
    public void setMitarbeiter(String s){
        mitarbeiter.setValue(s);
    }
    public void resetMitarbeiter() {
        mitarbeiter.setValue(null);
        timer.getValue().cancel();
    }
    public void setFiveMinutes(Boolean b) {
        fiveMinutes.setValue(b);
    }
}
