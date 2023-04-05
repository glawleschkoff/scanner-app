package de.glawleschkoff.scannerapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.remote.Repository;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<ResponseWrapper<List<String>>> responseMitarbeiter;
    private final Repository repository;

    public LoginViewModel(){
        repository = Repository.getInstance();
        //responseMitarbeiter = repository.getMitarbeiter();
        responseMitarbeiter = new MutableLiveData<>();
    }

    public LiveData<ResponseWrapper<List<String>>> getResponseMitarbeiter(){
        return responseMitarbeiter;
    }

    public void requestMitarbeiter(){
        repository.requestMitarbeiter(responseMitarbeiter);
    }
}
