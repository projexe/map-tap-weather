package com.projexe.maptapweather.model;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Interface to the Business model.
 * @author Simon Hutton
 * @version 1.0
 */

public interface IModelContract {

    void fetchWeatherFromApi(IApiResponse callbackOnApiResponseReceived, double lat, double lon);

    void readPersistedWeatherFromDB(IDbResponse callbackOnDBReadResponse);


    /**
     * Interface for API read callbacks
     */
    interface IApiResponse {
        void onApiSuccessReceived(@NonNull List<List> displayList);
        void onApiFailureReceived(int responseCode, String responseMessage);
        void onApiEmptyData(String responseMessage);
        void onApiOfflinePersistedData(List<List> displayList);
    }

    /**
     * Interface for Database read callbacks
     */
    interface IDbResponse {
        void onDbSuccessfulRead(List<List> displayList);
        void onDbEmptyData(String responseMessage);
        void onDbFailure(int responseCode, String responseMessage);
    }
}
