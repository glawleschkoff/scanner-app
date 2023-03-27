package de.glawleschkoff.scannerapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.glawleschkoff.scannerapp.remote.Repository;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<List<String>> responseMitarbeiter;
    private final Repository repository;

    public LoginViewModel(){
        repository = Repository.getInstance();
        responseMitarbeiter = repository.getMitarbeiter();
    }

    public LiveData<List<String>> getResponseMitarbeiter(){
        return responseMitarbeiter;
    }

    public void requestMitarbeiter(){
        repository.requestMitarbeiter();
    }
}
