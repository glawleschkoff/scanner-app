package de.glawleschkoff.scannerapp.viewmodel;

import android.widget.RelativeLayout;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.glawleschkoff.scannerapp.model.BauteilModel;
import de.glawleschkoff.scannerapp.remote.Repository;
import okhttp3.MultipartBody;
import retrofit2.Response;

public class InfoViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<Response<BauteilModel>> responseBauteil;

    public InfoViewModel(){
        repository = Repository.getInstance();
        responseBauteil = repository.getResponseBauteil();
    }
}
