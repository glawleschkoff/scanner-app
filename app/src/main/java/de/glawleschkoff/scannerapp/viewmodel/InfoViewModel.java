package de.glawleschkoff.scannerapp.viewmodel;

import android.widget.RelativeLayout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.glawleschkoff.scannerapp.model.BauteilModel;
import de.glawleschkoff.scannerapp.model.FeedbackModel;
import de.glawleschkoff.scannerapp.remote.Repository;
import retrofit2.Response;

public class InfoViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<Response<BauteilModel>> responseBauteil;
    private final MutableLiveData<FeedbackModel> responseFeedback;
    private final MutableLiveData<Integer> responseCounter;
    private final MutableLiveData<Boolean> bauteilDataDeprecated;

    public InfoViewModel(){
        repository = Repository.getInstance();
        responseBauteil = repository.getResponseBauteil();
        responseFeedback = repository.getResponseFeedback();
        responseCounter = repository.getResponseCounter();
        bauteilDataDeprecated = new MutableLiveData<>(Boolean.FALSE);
    }

    public void requestBauteil(String id){
        repository.requestBauteil(id);
    }
    public void requestFeedback(String name){
        repository.requestFeedback(name);
    }
    public LiveData<Response<BauteilModel>> getResponseBauteil(){
        return responseBauteil;
    }
    public LiveData<FeedbackModel> getResponseFeedback(){
        return responseFeedback;
    }
    public LiveData<Integer> getResponseCounter(){
        return responseCounter;
    }
    public void resetResponseCounter(){
        responseCounter.setValue(0);
    }
    public void resetResponseBauteil(){
        responseBauteil.setValue(null);
    }
    public void resetResponseFeedback(){
        responseFeedback.setValue(null);
    }
    public void setBauteilDataDeprecated(boolean b){
        bauteilDataDeprecated.setValue(b);
    }
}
