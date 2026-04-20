package de.glawleschkoff.scannerapp.remote;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;


import androidx.lifecycle.MutableLiveData;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.glawleschkoff.scannerapp.model.USERBauteilLogModel;
import de.glawleschkoff.scannerapp.model.USERALBDetailsModel;
import de.glawleschkoff.scannerapp.model.USERCNCFeedbackModel;
import de.glawleschkoff.scannerapp.model.USERKntFeedbackModel;
import de.glawleschkoff.scannerapp.model.USERKommWagenModel;
import de.glawleschkoff.scannerapp.model.LagerModel;
import de.glawleschkoff.scannerapp.model.MitarbeiterModel;
import de.glawleschkoff.scannerapp.model.USERPlattenlagerModel;
import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.util.BitmapCutter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Repository {


    private static Repository repository;
    private final HttpApi httpApi;
    private Target target;

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

    public void requestMaterialien(MutableLiveData<ResponseWrapper<List<String>>> responseMaterialien) {
        Call<List<String>> call = httpApi.getMaterialien();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(!response.isSuccessful()){
                    responseMaterialien.setValue(new ResponseWrapper<>(null,String.valueOf(response.code())));
                } else {
                    responseMaterialien.setValue(new ResponseWrapper<>(response.body(), null));
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                responseMaterialien.setValue(new ResponseWrapper<>(null,t.getMessage()));
            }
        });
    }

    public void requestUSERPlattenlager(String id, MutableLiveData<ResponseWrapper<USERPlattenlagerModel>> responseUSERPlattenlager){
        Call<USERPlattenlagerModel> call = httpApi.getUSERPlattenlager(id);
        call.enqueue(new Callback<USERPlattenlagerModel>() {
            @Override
            public void onResponse(Call<USERPlattenlagerModel> call, Response<USERPlattenlagerModel> response) {
                System.out.println("Body: "+response.body());
                System.out.println("Code :"+response.code());
                if(!response.isSuccessful()){
                    responseUSERPlattenlager.setValue(new ResponseWrapper<>(null,String.valueOf(response.code())));
                } else {
                    responseUSERPlattenlager.setValue(new ResponseWrapper<>(response.body(), null));
                }
            }
            @Override
            public void onFailure(Call<USERPlattenlagerModel> call, Throwable t) {
                System.out.println("Failure: "+t.getMessage());
                responseUSERPlattenlager.setValue(new ResponseWrapper<>(null,t.getMessage()));
            }
        });
    }

    public void updateUSERPlattenlager(Double plattenId, String lagerPlatz, Double lng, Double brt, String mz3, String auslagerId, String auslagerInfo, String auslagerDatum, Double menge) {
        Call<String> call = httpApi.updateUSERPlattenlager(plattenId,lagerPlatz,lng,brt,mz3,auslagerId,auslagerInfo,auslagerDatum,menge);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
    public void updateUSERPlattenlagerBearbeiten(Double plattenId, String lagerPlatz, Double lng, Double brt, String mz3, String auslagerId, String auslagerInfo, String auslagerDatum, Double menge) {
        Call<String> call = httpApi.updateUSERPlattenlagerBearbeiten(plattenId,lagerPlatz,lng,brt,mz3,auslagerId,auslagerInfo,auslagerDatum,menge);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public void updateUSERALBDetails(String exemplarNr, String scannerAnweisung) {
        Call<String> call = httpApi.updateUSERALBDetails(exemplarNr, scannerAnweisung);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Failure: "+t.getMessage());
            }
        });
    }

    public void insertUSERPlattenlager(Integer rowUserId, String matKurzzeichen, Double plattenId, String lagerplatz, String mz3, Double lng, Double brt) {
        Call<String> call = httpApi.insertUSERPlattenlager(rowUserId,matKurzzeichen,plattenId,lagerplatz,mz3,lng,brt);
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

    public void requestUSERALBDetails(String id, MutableLiveData<ResponseWrapper<USERALBDetailsModel>> responseUSERALBDetails){
        Call<USERALBDetailsModel> call = httpApi.getUSERALBDetails(id);
        call.enqueue(new Callback<USERALBDetailsModel>() {
            @Override
            public void onResponse(Call<USERALBDetailsModel> call, Response<USERALBDetailsModel> response) {
                if(!response.isSuccessful()){
                    responseUSERALBDetails.setValue(new ResponseWrapper<>(null,String.valueOf(response.code())));
                } else {
                    responseUSERALBDetails.setValue(new ResponseWrapper<>(response.body(),null));
                }
            }
            @Override
            public void onFailure(Call<USERALBDetailsModel> call, Throwable t) {
                responseUSERALBDetails.setValue(new ResponseWrapper<>(null,t.getMessage()));
            }
        });
    }

    public void requestUSERKommWagen(String auftrag, Integer losNr, MutableLiveData<ResponseWrapper<USERKommWagenModel>> responseUSERKommWagen){
        Call<USERKommWagenModel> call = httpApi.getUSERKommWagen(auftrag, losNr);
        call.enqueue(new Callback<USERKommWagenModel>() {
            @Override
            public void onResponse(Call<USERKommWagenModel> call, Response<USERKommWagenModel> response) {
                if(!response.isSuccessful()){
                    responseUSERKommWagen.setValue(new ResponseWrapper<>(null,String.valueOf(response.code())));
                } else {
                    responseUSERKommWagen.setValue(new ResponseWrapper<>(response.body(),null));
                }
            }

            @Override
            public void onFailure(Call<USERKommWagenModel> call, Throwable t) {
                responseUSERKommWagen.setValue(new ResponseWrapper<>(null,t.getMessage()));
            }
        });
    }

    public void requestUSERCNCFeedback(String id, MutableLiveData<ResponseWrapper<List<USERCNCFeedbackModel>>> responseUSERCNCFeedback){
        Call<List<USERCNCFeedbackModel>> call = httpApi.getUSERCNCFeedback(id);
        call.enqueue(new Callback<List<USERCNCFeedbackModel>>() {
            @Override
            public void onResponse(Call<List<USERCNCFeedbackModel>> call, Response<List<USERCNCFeedbackModel>> response) {
                if(!response.isSuccessful()){
                    responseUSERCNCFeedback.setValue(new ResponseWrapper<>(null,String.valueOf(response.code())));
                } else {
                    responseUSERCNCFeedback.setValue(new ResponseWrapper<>(response.body(),null));
                }
            }

            @Override
            public void onFailure(Call<List<USERCNCFeedbackModel>> call, Throwable t) {
                responseUSERCNCFeedback.setValue(new ResponseWrapper<>(null,t.getMessage()));
            }
        });
    }

    public void requestUSERKntFeedback(String id, MutableLiveData<ResponseWrapper<List<USERKntFeedbackModel>>> responseUSERKntFeedback){
        Call<List<USERKntFeedbackModel>> call = httpApi.getUSERKntFeedback(id);
        call.enqueue(new Callback<List<USERKntFeedbackModel>>() {
            @Override
            public void onResponse(Call<List<USERKntFeedbackModel>> call, Response<List<USERKntFeedbackModel>> response) {
                if(!response.isSuccessful()){
                    responseUSERKntFeedback.setValue(new ResponseWrapper<>(null,String.valueOf(response.code())));
                } else {
                    responseUSERKntFeedback.setValue(new ResponseWrapper<>(response.body(),null));
                }
            }

            @Override
            public void onFailure(Call<List<USERKntFeedbackModel>> call, Throwable t) {
                responseUSERKntFeedback.setValue(new ResponseWrapper<>(null,t.getMessage()));
            }
        });
    }

    public void requestUSERBauteilLog(String id, MutableLiveData<ResponseWrapper<List<USERBauteilLogModel>>> responseUSERBauteilLog){
        Call<List<USERBauteilLogModel>> call = httpApi.getUSERBauteilLog(id);
        call.enqueue(new Callback<List<USERBauteilLogModel>>() {
            @Override
            public void onResponse(Call<List<USERBauteilLogModel>> call, Response<List<USERBauteilLogModel>> response) {
                if(!response.isSuccessful()){
                    responseUSERBauteilLog.setValue(new ResponseWrapper<>(null,String.valueOf(response.code())));
                } else {
                    responseUSERBauteilLog.setValue(new ResponseWrapper<>(response.body(),null));
                }
            }

            @Override
            public void onFailure(Call<List<USERBauteilLogModel>> call, Throwable t) {
                responseUSERBauteilLog.setValue(new ResponseWrapper<>(null,t.getMessage()));
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
        target = new Target() {
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
        Picasso.get().invalidate("http://192.168.1.34:8080/api/v1/image/material/?name="+name);
        Picasso.get().load("http://192.168.1.34:8080/api/v1/image/material/?name="+name+"&timestamp="+System.currentTimeMillis())
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(target);

    }

}
