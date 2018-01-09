package com.projexe.maptapweather.model.api;


/**
 * Static Class (or mimics a static class as Java doesnt have top-level static classes)
 * Wraps the Retrofit.Builder for the generation of a Retrofit client to access the Open
 * Weather Map api and parse returning JSON
 * @author  Simon Hutton
 * @version 1.0
 */

public class ApiUtils {

    private static final String BASE_URL = "http://api.openweathermap.org/";

    public static IWeatherService getWeatherService() {
        return RetrofitClient.getClient(BASE_URL).create(IWeatherService.class);
    }
}