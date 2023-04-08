package de.glawleschkoff.scannerapp.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.glawleschkoff.scannerapp.model.RTEBFeedbackModel;
import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.model.RestteilModel;
import de.glawleschkoff.scannerapp.remote.Repository;

public class RTEBViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<RestteilModel> feedbackRestteil;
    private final MutableLiveData<ResponseWrapper<String>> responseFeedback;

    public RTEBViewModel(){
        repository = Repository.getInstance();
        feedbackRestteil = new MutableLiveData<>();
        responseFeedback = new MutableLiveData<>(new ResponseWrapper<>());
    }

    public LiveData<RestteilModel> getFeedbackRestteil() {
        return feedbackRestteil;
    }
    public LiveData<ResponseWrapper<String>> getResponseFeedback(){
        return responseFeedback;
    }

    public void requestFeedback(String name){
        repository.requestFeedback(name, responseFeedback);
    }
    public void createFeedback(RTEBFeedbackModel feedbackModel){
        repository.createFeedback(feedbackModel.toCsvName(), feedbackModel.toCsv());
    }

    public void setFeedbackRestteil(String feedback){
        feedbackRestteil.setValue(new RestteilModel(feedback));
    }
    public void setFeedbackRestteil(RestteilModel restteil){
        feedbackRestteil.setValue(restteil);
    }
}
