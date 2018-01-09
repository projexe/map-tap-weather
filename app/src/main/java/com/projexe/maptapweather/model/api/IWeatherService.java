package com.projexe.maptapweather.model.api;

import com.projexe.maptapweather.model.api.schema.CitiesInCycle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IWeatherService {

/**
 * Invokes Retrofit to sent request webserver and returns a response. Each call yields its own HTTP request and response pair. Calls may be executed synchronously with execute, or asynchronously with enqueue. In either case the call can be canceled at any time with cancel. A call that is busy writing its request or reading its response may receive a IOException; this is working as designed.
 */
    @GET("/data/2.5/find?")
    Call<CitiesInCycle> getCitiesInCycle(
                @Query("lat") double latitude,
                @Query("lon") double longitude,
                @Query("cnt") String count,
                @Query("lang") String language,
                @Query("APPID") String appId);

// // TODO: 28/11/2017 Handle situation where there is no data connection. Call is made, but onFailure not firing in fragment.

}
