package de.glawleschkoff.scannerapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.remote.Repository;

public class PRQMViewModel extends ViewModel {

    private final Repository repository;

    public PRQMViewModel(){
        repository = Repository.getInstance();
    }
}
