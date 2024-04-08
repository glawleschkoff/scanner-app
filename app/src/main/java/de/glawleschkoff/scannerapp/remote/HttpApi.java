package de.glawleschkoff.scannerapp.remote;

import java.sql.Date;
import java.util.List;

import de.glawleschkoff.scannerapp.model.USERBauteilLogModel;
import de.glawleschkoff.scannerapp.model.USERALBDetailsModel;
import de.glawleschkoff.scannerapp.model.USERCNCFeedbackModel;
import de.glawleschkoff.scannerapp.model.USERKntFeedbackModel;
import de.glawleschkoff.scannerapp.model.USERKommWagenModel;
import de.glawleschkoff.scannerapp.model.LagerModel;
import de.glawleschkoff.scannerapp.model.MitarbeiterModel;
import de.glawleschkoff.scannerapp.model.USERPlattenlagerModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HttpApi {

    @GET("api/v1/plattenlager")
    Call<USERPlattenlagerModel> getUSERPlattenlager(@Query("id") String id);

    @GET("api/v1/plattenlagerupdate")
    Call<String> updateUSERPlattenlager(@Query("plattenId") Double plattenId, @Query("lagerPlatz") String lagerPlatz, @Query("lng") Double lng, @Query("brt") Double brt, @Query("mz3") String mz3, @Query("auslagerId") String auslagerId, @Query("auslagerInfo") String auslagerInfo, @Query("auslagerDatum") String auslagerDatum, @Query("menge") Double menge);
    @GET("api/v1/plattenlagerupdatebearbeiten")
    Call<String> updateUSERPlattenlagerBearbeiten(@Query("plattenId") Double plattenId, @Query("lagerPlatz") String lagerPlatz, @Query("lng") Double lng, @Query("brt") Double brt, @Query("mz3") String mz3, @Query("auslagerId") String auslagerId, @Query("auslagerInfo") String auslagerInfo, @Query("auslagerDatum") String auslagerDatum, @Query("menge") Double menge);

    @GET("api/v1/bauteilupdate")
    Call<String> updateUSERALBDetails(@Query("exemplarNr") String exemplarNr, @Query("scannerAnweisung") String scannerAnweisung);

    @GET("api/v1/plattenlagerinsert")
    Call<String> insertUSERPlattenlager(@Query("rowUserId") Integer rowUserId, @Query("matKurzzeichen") String matKurzzeichen,
                                    @Query("plattenId") Double plattenId, @Query("lagerplatz") String lagerplatz,
                                    @Query("mz3") String mz3, @Query("lng") Double lng, @Query("brt") Double brt);

    @GET("api/v1/plattenlagermax")
    Call<Double> getMaxPlattenID();

    @GET("api/v1/materialien")
    Call<List<String>> getMaterialien();

    @GET("api/v1/lager")
    Call<LagerModel> getLager(@Query("id") String id);

    @GET("api/v1/bauteil")
    Call<USERALBDetailsModel> getUSERALBDetails(@Query("id") String id);

    @GET("api/v1/kommwagen")
    Call<USERKommWagenModel> getUSERKommWagen(@Query("auftrag") String auftrag);

    @GET("api/v1/mitarbeiter")
    Call<List<MitarbeiterModel>> getMitarbeiter();

    @GET("api/v1/cncfeedback")
    Call<List<USERCNCFeedbackModel>> getUSERCNCFeedback(@Query("id") String id);

    @GET("api/v1/kntfeedback")
    Call<List<USERKntFeedbackModel>> getUSERKntFeedback(@Query("id") String id);

    @GET("api/v1/bauteillog")
    Call<List<USERBauteilLogModel>> getUSERBauteilLog(@Query("id") String id);
}
