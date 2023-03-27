package de.glawleschkoff.scannerapp.remote;

import java.util.List;

import de.glawleschkoff.scannerapp.model.BauteilModel;
import de.glawleschkoff.scannerapp.model.MitarbeiterModel;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HttpApi {

    @GET("api/v1/bauteil")
    Call<BauteilModel> getBauteil(@Query("id") String id);


    @Multipart
    @POST("uploadFile")
    Call<ResponseBody> createFeedback(@Part MultipartBody.Part file);

    @GET("downloadFile/{fileName}")
    Call<ResponseBody> getFeedback(@Path("fileName") String name);

    @GET("api/v1/mitarbeiter")
    Call<List<MitarbeiterModel>> getMitarbeiter();
}
