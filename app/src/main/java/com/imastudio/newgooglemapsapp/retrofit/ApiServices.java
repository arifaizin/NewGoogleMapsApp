package com.imastudio.newgooglemapsapp.retrofit;

import com.imastudio.newgooglemapsapp.response.ResponseMaps;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {
    @GET("nearbysearch/json?type=hospital&radius=5000")
    Call<ResponseMaps> getDataMaps(
            @Query("location") String location,
            @Query("key") String key
    );


}
