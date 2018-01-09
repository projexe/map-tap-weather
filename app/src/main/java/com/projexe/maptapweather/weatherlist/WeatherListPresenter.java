package com.projexe.maptapweather.weatherlist;

import android.support.annotation.NonNull;

import com.projexe.maptapweather.model.IModelContract;

import java.util.List;

/**
 **This is the Presenter layer for the the WeatherList View (using the Model View Presenter anchitecture).
 *
 * Implements IWeatherListContract.PresenterListener interface enabling View to communicate with it
 * Passed View reference in Constructor enabling this Presenter to communicate with the View
 * Passed Injection of the Model/Repository Injection in the constructor to enable communication with the Model
 * All the presentation logic for the WeatherList goes in here
 * @author Simon Hutton
 * @version 1.0
 */

public class WeatherListPresenter implements IWeatherListContract.PresenterListener,
                                             IModelContract.IApiResponse,
                                             IModelContract.IDbResponse{

    private final IWeatherListContract.View mView;
    private final IModelContract mModel;

    /**
     * Constructor. Sets up callbacks to Repository and View.
     * @param view VIEW reference
     * @param model MODEL reference
     */
    public WeatherListPresenter(@NonNull IWeatherListContract.View view,
                                @NonNull IModelContract model) {
        this.mModel = model;
        this.mView = view;
    }


    /**
     *  Fetch the weather data from Persisted weather data via the data model.
     */
    @Override
    public void fetchPersistedWeather() {
        mModel.readPersistedWeatherFromDB(this);
    }

    /**
     *  Fetch the weather data from the Weather API via the data model.
     *  if the model cannot connect to the internet
     *  then the weather data should be returned from persisted storage.
     * @param lat latitude of the desired weather location
     * @param lon longitude of the desired weather location
     */
    @Override
    public void fetchWeather(double lat, double lon) {
        // onApiResponseReceiced will callback to onApiResponseCallbackreceived handler in this class
        mModel.fetchWeatherFromApi(this, lat, lon);
    }

    /**
     * Callback handler for a successful response received from the Open Weather API*
     * @param displayList List of data to be displayed
     */
    @Override
    public void onApiSuccessReceived(@NonNull List<List> displayList) {
        mView.displayWeatherData(displayList, false);
    }

    /**
     * Callback handler for a failure response received from the Open Weather API
     * @param responseCode code associated with the failure
     * @param responseMessage message associated with the failure
     */
    @Override
    public void onApiFailureReceived(int responseCode, String responseMessage) {
        mView.displayToast(responseMessage);
    }

    /**
     * Callback handler for a successful response received from the Open Weather API,
     * but no city data returned
     * @param responseMessage message associated with the callback
     */
    @Override
    public void onApiEmptyData(String responseMessage) {
        mView.displayMaterialDesignEmptyState(responseMessage);
    }

    /**
     * Callback handler for an unsuccessful response from the Open Weather API, because the device
     * has no internet access. The displaylist has therefore been populated from persisted data
     * @param displayList List of data to be displayed
     */
    @Override
    public void onApiOfflinePersistedData(List<List> displayList) {
        mView.displayWeatherData(displayList, true);
    }

    /**
     * Callback handler for a successful read of the persisted Weather database
     * @param displayList List of data to be displayed
     */
    @Override
    public void onDbSuccessfulRead(List<List> displayList) {
        mView.displayWeatherData(displayList, true);
    }

    /**
     * Callback handler for persisted data returning an empty dataset
     * @param responseMessage Message for dispplay to the user
     */
    @Override
    public void onDbEmptyData(String responseMessage) {
        mView.displayMaterialDesignEmptyState(responseMessage);
    }

    /**
     * Callback handler for an unsuccessful read of the persisted Weather database
     * @param responseCode error response code
     * @param responseMessage error message
     */
    @Override
    public void onDbFailure(int responseCode, String responseMessage) {
        mView.displayToast(responseMessage);
    }
}
