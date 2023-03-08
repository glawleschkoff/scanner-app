package de.glawleschkoff.scannerapp.old;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BauteilRepository {

    private HttpApi httpApi;
    private MutableLiveData<String> data = new MutableLiveData<>();

    public BauteilRepository(){
        Retrofit retrofit = RetrofitInstance.getInstance();
        httpApi = retrofit.create(HttpApi.class);
    }

    public void getData(){
        Call<String> call = httpApi.getBauteil(83);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    data.setValue("Code: " + response.code());
                    System.out.println(response.code());
                } else {
                    data.setValue(response.body());
                    System.out.println(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                data.setValue(t.getMessage());
            }
        });
    }

    public MutableLiveData<String> bindData(){
        return data;
    }
}
