package de.glawleschkoff.scannerapp;

import io.reactivex.Flowable;
import retrofit2.Retrofit;

public class BauteilRepository {

    private HttpApi httpApi;

    public BauteilRepository(){
        Retrofit retrofit = RetrofitInstance.getInstance();
        httpApi = retrofit.create(HttpApi.class);
    }

    public Flowable<BauteilModel> getBauteilFlowable(int id){
        return httpApi.getBauteil(id);
    }
}
