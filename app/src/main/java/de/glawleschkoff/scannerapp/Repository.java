package de.glawleschkoff.scannerapp;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Repository {

    private final HttpApi httpApi;
    private final MutableLiveData<Response<BauteilModel>> responseMutableLiveData = new MutableLiveData<>();

    public Repository(){
        Retrofit retrofit = RetrofitInstance.getInstance();
        httpApi = retrofit.create(HttpApi.class);
    }

    public void getBauteil(String id){
        Call<BauteilModel> call = httpApi.getBauteil(id);
        call.enqueue(new Callback<BauteilModel>() {
            @Override
            public void onResponse(Call<BauteilModel> call, Response<BauteilModel> response) {
                if(!response.isSuccessful()){
                    responseMutableLiveData.setValue(response);
                } else {
                    responseMutableLiveData.setValue(response);
                }
            }
            @Override
            public void onFailure(Call<BauteilModel> call, Throwable t) {
                //Toast.makeText(this.getContext(), data, Toast.LENGTH_SHORT).show();
                responseMutableLiveData.setValue(Response.error(999, ResponseBody.create(
                        MediaType.parse("application/json"),
                        ""
                )));
            }
        });
    }

    public void createFeedback(FeedbackModel feedbackModel){
        Call<FeedbackModel> call = httpApi.createFeedback(feedbackModel);
        call.enqueue(new Callback<FeedbackModel>() {
            @Override
            public void onResponse(Call<FeedbackModel> call, Response<FeedbackModel> response) {
                System.out.println("success");
            }

            @Override
            public void onFailure(Call<FeedbackModel> call, Throwable t) {
                System.out.println("fail");
            }
        });
    }

    public MutableLiveData<Response<BauteilModel>> getResponseMutableLiveData() {
        return responseMutableLiveData;
    }
}
