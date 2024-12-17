package com.example.planescaper.unsplash;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UnsplashAPI {
    @GET("photos/random")
    Call<UnsplashPhoto> getRandomPhoto(@Query("query") String query, @Query("client_id") String apiKey);
}
