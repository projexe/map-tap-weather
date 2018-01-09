package com.projexe.maptapweather.model.api;

import android.support.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A static class (or mimics a static class as Java doesnt have top-level static classes)
 * Wraps the Retrofit.Builder for the generation of a Retrofit client to access the Open
 * Weather Map api and parse returning JSON
 * Once instantiated, retrofit remains in memory for reuse
 * @author  Simon Hutton
 * @version 1.0
 */
public class RetrofitClient {

    private static Retrofit retrofit = null;

    private RetrofitClient() {
        // private constructor prevents RetrofitClient instance being instatiated by mistake
    }

    public static Retrofit getClient(@NonNull String baseUrl) {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)  // base URL of of the API call
                    .addConverterFactory(GsonConverterFactory.create()) // Add a GSON based JSON convertor for deserialisation
                    .build();
        }
        return retrofit;
    }
}