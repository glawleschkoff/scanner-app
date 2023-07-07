package de.glawleschkoff.scannerapp.remote;

import java.util.List;

import de.glawleschkoff.scannerapp.model.BauteilLogModel;
import de.glawleschkoff.scannerapp.model.BauteilModel;
import de.glawleschkoff.scannerapp.model.CNCFeedbackModel;
import de.glawleschkoff.scannerapp.model.KntFeedbackModel;
import de.glawleschkoff.scannerapp.model.LagerModel;
import de.glawleschkoff.scannerapp.model.MitarbeiterModel;
import de.glawleschkoff.scannerapp.model.PlattenlagerModel;
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

    @GET("api/v1/plattenlager")
    Call<PlattenlagerModel> getPlattenlager(@Query("id") String id);

    @GET("api/v1/plattenlagerupdate")
    Call<String> updatePlattenlager(@Query("plattenId") Double plattenId, @Query("lagerPlatz") String lagerPlatz, @Query("lng") Double lng, @Query("brt") Double brt, @Query("mz3") String mz3);

    @GET("api/v1/bauteilupdate")
    Call<String> updateBauteil(@Query("exemplarNr") String exemplarNr, @Query("scannerAnweisung") String scannerAnweisung);

    @GET("api/v1/plattenlagerinsert")
    Call<String> insertPlattenlager(@Query("rowUserId") Integer rowUserId, @Query("matKurzzeichen") String matKurzzeichen,
                                    @Query("plattenId") Double plattenId, @Query("lagerplatz") String lagerplatz,
                                    @Query("mz3") String mz3, @Query("lng") Double lng, @Query("brt") Double brt);

    @GET("api/v1/plattenlagermax")
    Call<Double> getMaxPlattenID();

    @GET("api/v1/materialien")
    Call<List<String>> getMaterialien();

    @GET("api/v1/lager")
    Call<LagerModel> getLager(@Query("id") String id);

    @GET("api/v1/bauteil")
    Call<BauteilModel> getBauteil(@Query("id") String id);

    @Multipart
    @POST("api/v1/feedback")
    Call<ResponseBody> createFeedback(@Part MultipartBody.Part file);

    @GET("api/v1/feedback")
    Call<ResponseBody> getFeedback(@Query("name") String name);

    @GET("api/v1/mitarbeiter")
    Call<List<MitarbeiterModel>> getMitarbeiter();

    @GET("api/v1/cncfeedback")
    Call<List<CNCFeedbackModel>> getCNCFeedback(@Query("id") String id);

    @GET("api/v1/kntfeedback")
    Call<List<KntFeedbackModel>> getKntFeedback(@Query("id") String id);

    @GET("api/v1/bauteillog")
    Call<List<BauteilLogModel>> getBauteilLog(@Query("id") String id);
}
