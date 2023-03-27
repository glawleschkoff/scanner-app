package de.glawleschkoff.scannerapp.remote;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.glawleschkoff.scannerapp.model.BauteilModel;
import de.glawleschkoff.scannerapp.model.FeedbackModel;
import de.glawleschkoff.scannerapp.model.MitarbeiterModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Repository {


    private static Repository repository;
    private final HttpApi httpApi;
    private final MutableLiveData<Response<BauteilModel>> responseBauteil = new MutableLiveData<>();
    private final MutableLiveData<FeedbackModel> responseFeedback = new MutableLiveData<>();
    private final MutableLiveData<Integer> responseCounter = new MutableLiveData<>(0);
    private final MutableLiveData<List<String>> mitarbeiter = new MutableLiveData<>();

    public static synchronized Repository getInstance(){
        if(repository==null){
            repository = new Repository();
        }
        return repository;
    }
    private Repository(){
        Retrofit retrofit = RetrofitInstance.getInstance();
        httpApi = retrofit.create(HttpApi.class);
    }

    public void requestBauteil(String id){
        Call<BauteilModel> call = httpApi.getBauteil(id);
        call.enqueue(new Callback<BauteilModel>() {
            @Override
            public void onResponse(Call<BauteilModel> call, Response<BauteilModel> response) {
                if(!response.isSuccessful()){
                    responseBauteil.setValue(response);
                    responseCounter.setValue(responseCounter.getValue()+1);
                } else {
                    responseBauteil.setValue(response);
                    responseCounter.setValue(responseCounter.getValue()+1);
                }
            }
            @Override
            public void onFailure(Call<BauteilModel> call, Throwable t) {
                //Toast.makeText(this.getContext(), data, Toast.LENGTH_SHORT).show();
                responseBauteil.setValue(Response.error(999, ResponseBody.create(
                        MediaType.parse("application/json"),
                        ""
                )));
                responseCounter.setValue(responseCounter.getValue()+1);
            }
        });
    }

    public void createFeedback(FeedbackModel feedbackModel){
        Call<ResponseBody> call = httpApi.createFeedback(
                MultipartBody.Part.createFormData(
                        "file",
                        feedbackModel.toCsvName(),
                        RequestBody.create(MediaType.parse("text/csv"), feedbackModel.toCsv())
                )
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("code: "+response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("failure: "+t.getMessage());
            }
        });
    }

    public void requestFeedback(String name){
        Call<ResponseBody> call = httpApi.getFeedback(name);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    responseFeedback.setValue(null);
                    responseCounter.setValue(responseCounter.getValue()+1);
                } else {
                    try {
                        String s = new String(response.body().bytes());
                        responseFeedback.setValue(new FeedbackModel(s));

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    responseCounter.setValue(responseCounter.getValue()+1);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                responseFeedback.setValue(null);
                responseCounter.setValue(responseCounter.getValue()+1);
            }
        });

    }

    public void requestMitarbeiter(){
        Call<List<MitarbeiterModel>> call = httpApi.getMitarbeiter();
        call.enqueue(new Callback<List<MitarbeiterModel>>() {
            @Override
            public void onResponse(Call<List<MitarbeiterModel>> call, Response<List<MitarbeiterModel>> response) {
                if(!response.isSuccessful()){

                } else {
                    mitarbeiter.setValue(response.body().stream().map(x -> x.getKurzzeichen()).filter(x -> !Arrays.asList("1","2","3","4").contains(x)).sorted().collect(Collectors.toList()));
                }
            }

            @Override
            public void onFailure(Call<List<MitarbeiterModel>> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<Response<BauteilModel>> getResponseBauteil() {
        return responseBauteil;
    }
    public MutableLiveData<FeedbackModel> getResponseFeedback(){
        return responseFeedback;
    }
    public MutableLiveData<Integer> getResponseCounter(){
        return responseCounter;
    }
    public MutableLiveData<List<String>> getMitarbeiter(){
        return mitarbeiter;
    }
}
