package de.glawleschkoff.scannerapp;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HttpApi {

    @GET("api/v1/bauteil")
    Flowable<BauteilModel> getBauteil(@Query("id") int id);
}
