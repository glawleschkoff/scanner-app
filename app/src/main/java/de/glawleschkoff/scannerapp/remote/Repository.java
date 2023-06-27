package de.glawleschkoff.scannerapp.remote;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.Toast;


import androidx.lifecycle.MutableLiveData;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.glawleschkoff.scannerapp.model.BauteilLogModel;
import de.glawleschkoff.scannerapp.model.BauteilModel;
import de.glawleschkoff.scannerapp.model.CNCFeedbackModel;
import de.glawleschkoff.scannerapp.model.KntFeedbackModel;
import de.glawleschkoff.scannerapp.model.LagerModel;
import de.glawleschkoff.scannerapp.model.MitarbeiterModel;
import de.glawleschkoff.scannerapp.model.PlattenlagerModel;
import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.util.BitmapCutter;
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
    private Target target;
    private Target target2;

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

    public void requestPlattenlager(String id, MutableLiveData<ResponseWrapper<PlattenlagerModel>> responsePlattenlager){
        Call<PlattenlagerModel> call = httpApi.getPlattenlager(id);
        call.enqueue(new Callback<PlattenlagerModel>() {
            @Override
            public void onResponse(Call<PlattenlagerModel> call, Response<PlattenlagerModel> response) {
                if(!response.isSuccessful()){
                    responsePlattenlager.setValue(new ResponseWrapper<>(null,String.valueOf(response.code())));
                } else {
                    responsePlattenlager.setValue(new ResponseWrapper<>(response.body(), null));
                }
            }
            @Override
            public void onFailure(Call<PlattenlagerModel> call, Throwable t) {
                responsePlattenlager.setValue(new ResponseWrapper<>(null,t.getMessage()));
            }
        });
    }

    public void updatePlattenlager(Double plattenId, String lagerPlatz, Double lng, Double brt, String mz3) {
        Call<String> call = httpApi.updatePlattenlager(plattenId,lagerPlatz,lng,brt,mz3);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public void insertPlattenlager(Integer rowUserId, String matKurzzeichen, Double plattenId, String lagerplatz, String mz3, Double lng, Double brt) {
        Call<String> call = httpApi.insertPlattenlager(rowUserId,matKurzzeichen,plattenId,lagerplatz,mz3,lng,brt);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public void getMaxPlattenID(MutableLiveData<ResponseWrapper<Double>> maxPlattenID) {
        Call<Double> call = httpApi.getMaxPlattenID();
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if(!response.isSuccessful()){
                    maxPlattenID.setValue(new ResponseWrapper<>(null,String.valueOf(response.code())));
                } else {
                    maxPlattenID.setValue(new ResponseWrapper<>(response.body(), null));
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                maxPlattenID.setValue(new ResponseWrapper<>(null,t.getMessage()));
            }
        });
    }

    public void requestLager(String id, MutableLiveData<ResponseWrapper<LagerModel>> responseLager){
        Call<LagerModel> call = httpApi.getLager(id);
        call.enqueue(new Callback<LagerModel>() {
            @Override
            public void onResponse(Call<LagerModel> call, Response<LagerModel> response) {
                if(!response.isSuccessful()){
                    responseLager.setValue(new ResponseWrapper<>(null,String.valueOf(response.code())));
                } else {
                    responseLager.setValue(new ResponseWrapper<>(response.body(), null));
                }
            }
            @Override
            public void onFailure(Call<LagerModel> call, Throwable t) {
                responseLager.setValue(new ResponseWrapper<>(null,t.getMessage()));
            }
        });
    }

    public void requestBauteil(String id, MutableLiveData<ResponseWrapper<BauteilModel>> responseBauteil){
        Call<BauteilModel> call = httpApi.getBauteil(id);
        call.enqueue(new Callback<BauteilModel>() {
            @Override
            public void onResponse(Call<BauteilModel> call, Response<BauteilModel> response) {
                if(!response.isSuccessful()){
                    responseBauteil.setValue(new ResponseWrapper<>(null,String.valueOf(response.code())));
                } else {
                    responseBauteil.setValue(new ResponseWrapper<>(response.body(),null));
                }
            }
            @Override
            public void onFailure(Call<BauteilModel> call, Throwable t) {
                responseBauteil.setValue(new ResponseWrapper<>(null,t.getMessage()));
            }
        });
    }

    public void requestCNCFeedback(String id, MutableLiveData<ResponseWrapper<List<CNCFeedbackModel>>> responseCNCFeedback){
        Call<List<CNCFeedbackModel>> call = httpApi.getCNCFeedback(id);
        call.enqueue(new Callback<List<CNCFeedbackModel>>() {
            @Override
            public void onResponse(Call<List<CNCFeedbackModel>> call, Response<List<CNCFeedbackModel>> response) {
                if(!response.isSuccessful()){
                    responseCNCFeedback.setValue(new ResponseWrapper<>(null,String.valueOf(response.code())));
                } else {
                    responseCNCFeedback.setValue(new ResponseWrapper<>(response.body(),null));
                }
            }

            @Override
            public void onFailure(Call<List<CNCFeedbackModel>> call, Throwable t) {
                responseCNCFeedback.setValue(new ResponseWrapper<>(null,t.getMessage()));
            }
        });
    }

    public void requestKntFeedback(String id, MutableLiveData<ResponseWrapper<List<KntFeedbackModel>>> responseKntFeedback){
        Call<List<KntFeedbackModel>> call = httpApi.getKntFeedback(id);
        call.enqueue(new Callback<List<KntFeedbackModel>>() {
            @Override
            public void onResponse(Call<List<KntFeedbackModel>> call, Response<List<KntFeedbackModel>> response) {
                if(!response.isSuccessful()){
                    responseKntFeedback.setValue(new ResponseWrapper<>(null,String.valueOf(response.code())));
                } else {
                    responseKntFeedback.setValue(new ResponseWrapper<>(response.body(),null));
                }
            }

            @Override
            public void onFailure(Call<List<KntFeedbackModel>> call, Throwable t) {
                responseKntFeedback.setValue(new ResponseWrapper<>(null,t.getMessage()));
            }
        });
    }

    public void requestBauteilLog(String id, MutableLiveData<ResponseWrapper<List<BauteilLogModel>>> responseBauteilLog){
        Call<List<BauteilLogModel>> call = httpApi.getBauteilLog(id);
        call.enqueue(new Callback<List<BauteilLogModel>>() {
            @Override
            public void onResponse(Call<List<BauteilLogModel>> call, Response<List<BauteilLogModel>> response) {
                if(!response.isSuccessful()){
                    responseBauteilLog.setValue(new ResponseWrapper<>(null,String.valueOf(response.code())));
                } else {
                    responseBauteilLog.setValue(new ResponseWrapper<>(response.body(),null));
                }
            }

            @Override
            public void onFailure(Call<List<BauteilLogModel>> call, Throwable t) {
                responseBauteilLog.setValue(new ResponseWrapper<>(null,t.getMessage()));
            }
        });
    }

    public void createFeedback(String csvName, String csvContent){
        Call<ResponseBody> call = httpApi.createFeedback(
                MultipartBody.Part.createFormData(
                        "file",
                        csvName,
                        RequestBody.create(MediaType.parse("text/csv"), csvContent)
                )
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void requestFeedback(String name, MutableLiveData<ResponseWrapper<String>> responseFeedback){
        Call<ResponseBody> call = httpApi.getFeedback(name);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    responseFeedback.setValue(new ResponseWrapper<>(null,String.valueOf(response.code())));
                } else {
                    try {
                        responseFeedback.setValue(new ResponseWrapper<>(new String(response.body().bytes()),null));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                responseFeedback.setValue(new ResponseWrapper<>(null,t.getMessage()));
            }
        });

    }

    public void requestMitarbeiter(MutableLiveData<ResponseWrapper<List<String>>> responseMitarbeiter){
        Call<List<MitarbeiterModel>> call = httpApi.getMitarbeiter();
        call.enqueue(new Callback<List<MitarbeiterModel>>() {
            @Override
            public void onResponse(Call<List<MitarbeiterModel>> call, Response<List<MitarbeiterModel>> response) {
                if(!response.isSuccessful()){
                    responseMitarbeiter.setValue(new ResponseWrapper<>(null,String.valueOf(response.code())));
                } else {
                    responseMitarbeiter.setValue(new ResponseWrapper<>(response.body()
                            .stream().map(x -> x.getKurzzeichen())
                            .filter(x -> !Arrays.asList("1","2","3","4").contains(x)).sorted()
                            .collect(Collectors.toList()),null));
                }
            }

            @Override
            public void onFailure(Call<List<MitarbeiterModel>> call, Throwable t) {
                responseMitarbeiter.setValue(new ResponseWrapper<>(null,t.getMessage()));
            }
        });
    }

    public void requestBitmapBauteil(String id, String name, MutableLiveData<ResponseWrapper<Bitmap>> responseBitmap){
        System.out.println("hier");
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Bitmap b = BitmapCutter.imageWithMargin(bitmap, -1,0);
                responseBitmap.setValue(new ResponseWrapper<>(b,null));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                responseBitmap.setValue(new ResponseWrapper<>(null,e.getMessage()));
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.get().load("http://192.168.1.34:8080/api/v1/image/bauteil/?id="+id+"&name="+name+".jpg")
                .into(target);
    }

    public void requestBitmapMaterial(String name, MutableLiveData<ResponseWrapper<Bitmap>> responseBitmap){
        target2 = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                System.out.println("bitmap succeded");
                Bitmap b = BitmapCutter.imageWithMargin(bitmap, -1,0);
                responseBitmap.setValue(new ResponseWrapper<>(b,null));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                System.out.println(e.getMessage());
                responseBitmap.setValue(new ResponseWrapper<>(null,e.getMessage()));
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                System.out.println("bitmap prepare");
            }
        };
        System.out.println(name);
        Picasso.get().load("http://192.168.1.34:8080/api/v1/image/material/?name="+name)
                .into(target2);
    }

}
