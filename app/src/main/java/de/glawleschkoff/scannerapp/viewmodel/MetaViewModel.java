package de.glawleschkoff.scannerapp.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.remote.Repository;


public class MetaViewModel extends ViewModel {

    private final MutableLiveData<String> mitarbeiter;
    private final MutableLiveData<String> scannerNr;
    private final Repository repository;

    public MetaViewModel(){
        repository = Repository.getInstance();
        mitarbeiter = new MutableLiveData<>();
        scannerNr = new MutableLiveData<>("001");
    }

    public LiveData<String> getMitarbeiter(){
        return mitarbeiter;
    }
    public LiveData<String> getScannerNr() {
        return scannerNr;
    }
    public void setMitarbeiter(String s){
        mitarbeiter.setValue(s);
    }
}
