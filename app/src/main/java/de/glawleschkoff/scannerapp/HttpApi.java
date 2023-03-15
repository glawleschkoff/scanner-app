package de.glawleschkoff.scannerapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HttpApi {

    @GET("api/v1/bauteil")
    Call<BauteilModel> getBauteil(@Query("id") String id);

    @POST("api/v1/feedback")
    Call<FeedbackModel> createFeedback(@Body FeedbackModel feedbackModel);
}
