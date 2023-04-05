package de.glawleschkoff.scannerapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.glawleschkoff.scannerapp.remote.Repository;

public class RTEBViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<String> feedbackRestteil;

    public RTEBViewModel(){
        repository = Repository.getInstance();
        feedbackRestteil = new MutableLiveData<>();
    }

    public LiveData<String> getFeedbackRestteil() {
        return feedbackRestteil;
    }
    public void setFeedbackRestteil(String feedback){
        feedbackRestteil.setValue(feedback);
    }
}
