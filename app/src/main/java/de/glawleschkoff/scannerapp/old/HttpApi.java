package de.glawleschkoff.scannerapp.old;

import de.glawleschkoff.scannerapp.old.BauteilModel;
import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HttpApi {

    @GET("api/v1/bauteil")
    Call<String> getBauteil(@Query("id") int id);
}
