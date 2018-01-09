package com.projexe.maptapweather.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.projexe.maptapweather.R;
import com.projexe.maptapweather.model.api.ApiUtils;
import com.projexe.maptapweather.model.api.IWeatherService;
import com.projexe.maptapweather.model.api.schema.CitiesInCycle;
import com.projexe.maptapweather.model.api.schema.CityList;
import com.projexe.maptapweather.model.repository.WeatherData;
import com.projexe.maptapweather.model.repository.WeatherPersistenceDatabase;

import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * The model class. Implements the business rulse for the app
 * Controls calls to the external actors, the Open weather Maps API and the persisted storage,
 * handles the data returned and makes callbacks to the presenter
 * @author Simon Hutton
 * @version 1.0
 */
public class ModelImplementation implements IModelContract {
    //Constants
    private final String TAG = "ModelImplementation";
    private final String SEPARATOR = "/ ";  // Seperates min and max degrees value on display

    private final String UNITS_METRIC = "metric";
    private final String UNITS_IMPERIAL = "imperial";

    private final String DEFAULT_NUMBER_OF_CITIES = "15";

    private final Context mContext;
    private IWeatherService mWeatherService;
    private final NumberFormat mFormatter = new DecimalFormat("#0.0"); // Display to one dp

    /**
     * CONTRUCTOR : Needs to be passed the context of the calling View in order to be able to read
     * shared preferences
     * @param ctx view context
     */
    public ModelImplementation(Context ctx) {
        this.mContext = ctx;
    }


    /**
     * Get an instance of the Weather Service API, then call it to return the 'cities in cycle'
     * weather data. Callback to report responses.
     * Note that the temperature will always be obtained in Kelvin for local conversion
     * @param callback callback handler
     * @param lat latitude of root
     * @param lon longitude of root
     */
    @Override
    public void fetchWeatherFromApi(IApiResponse callback, double lat, double lon) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        final String sCount = preferences.getString(mContext.getString(R.string.pref_key_number_of_cities), DEFAULT_NUMBER_OF_CITIES).trim();

        // get a reference to the retrofit client
        if (mWeatherService == null) {
            mWeatherService = ApiUtils.getWeatherService();
        }

        // Call the Retrofit service to get cities in cycle. Call is executed asynchronously.
        try {
            mWeatherService.getCitiesInCycle(lat,
                                lon,
                                sCount,
                                null,
                                mContext.getResources().getString(R.string.open_weather_maps_api_key))
                            .enqueue(new Callback<CitiesInCycle>() {

                @Override
                public void onResponse(@NonNull Call<CitiesInCycle> call, @NonNull Response<CitiesInCycle> response) {

                    final List<CityList> cityList = response.body().getCityList();

                    if (response.isSuccessful()) {
                        final int cityListSize = cityList.size();
                        Log.d(TAG, "API Weather data received successfully. " + cityListSize + " cities returned");

                        if (cityListSize == 0) {
                            persistApiWeather(cityList);
                            callback.onApiEmptyData(mContext.getString(R.string.message_no_weather_data_to_show));
                        } else {
                            // extract the data we want to return to the presenter
                            final ArrayList dataList = formatWeatherDisplayDataFromAPI(cityList);
                            callback.onApiSuccessReceived(dataList);
                            // TODO: 30/11/2017 run on a seperate thread
                            persistApiWeather(cityList);
                        }

                    } else {
                        final String message = mContext.getString(R.string.error_mess_api_unsuccessful_response) + response.code();
                        Log.d(TAG, message );
                        callback.onApiFailureReceived(response.code(), message);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<CitiesInCycle> call, @NonNull Throwable retrofitError) {
                    if (retrofitError instanceof UnknownHostException) {
                        // Device offline error
                        callback.onApiFailureReceived(998, mContext.getString(R.string.error_mess_api_no_internet));
                } else {
                        // report the failure to the calling process
                        final String message =  mContext.getString(R.string.error_mess_api_failure_response) + retrofitError.getLocalizedMessage();
                        Log.d(TAG, message);
                        Crashlytics.logException(retrofitError);
                        callback.onApiFailureReceived(999, message);
                    }
                }
            });
        } catch (NullPointerException e) {
            final String message = mContext.getString(R.string.error_mess_api_null_pointer) + e.getLocalizedMessage();
            Log.d(TAG, message);
            callback.onApiFailureReceived(997, message);
        }
    }


    /**
     * Take the citylist returned from the Api and format it for display by the View
     * @param cityList returned from the Weather API
     * @return ArrayList just containing items formatted for display on the View
     */
    private ArrayList<List> formatWeatherDisplayDataFromAPI(List<CityList> cityList) {
        ArrayList<List> rowsToDisplay = new ArrayList<>();
        ArrayList<String> columnData;

        // iterate along the list returned from the api writing formatted values
        // to rowsToDisplay
        for (CityList item : cityList) {
            columnData = new ArrayList<>();
            columnData.add(item.getName());
            columnData.add(item.getWeather().get(0).getDescription());

            final double dMin = convertTemp(item.getMain().getTempMin());
            final double dMax = convertTemp(item.getMain().getTempMax());
            if (dMax == dMin) {
                columnData.add(mFormatter.format(dMax) + displayTemperatureUnits());
            } else {
                final String sRangeString = mFormatter.format(dMin) + SEPARATOR + mFormatter.format(dMax) + displayTemperatureUnits();
                columnData.add(sRangeString);
            }

            //supplementary column data - not currently displayed
            columnData.add(String.valueOf(item.getMain().getPressure()));
            columnData.add(String.valueOf(item.getMain().getHumidity()));
            columnData.add(String.valueOf(item.getWind().getSpeed()));
            columnData.add(String.valueOf(item.getWind().getDeg()));
            columnData.add(String.valueOf(item.getWeather().get(0).getIcon()));

            rowsToDisplay.add(columnData);
        }
        return rowsToDisplay;
    }
    /**
     * Take the list of WeatherData returned from the persistence database and format it for
     * display by the View
     * @param tableList returned from the Weather API
     * @return ArrayList just containing items formatted for display on the View
     */
    private ArrayList<List> formatWeatherDisplayDataFromDatabase(List<WeatherData> tableList) {
        ArrayList<List> rowsToDisplay = new ArrayList<>();
        ArrayList<String> columnData;

        if (tableList == null) return null;

        for (WeatherData item : tableList) {
            columnData = new ArrayList<>();
            columnData.add(item.cityName);
            columnData.add(item.description);

            final double dMin = convertTemp(item.minTemp);
            final double dMax = convertTemp(item.maxTemp);
            if (dMax == dMin) {
                columnData.add(mFormatter.format(dMax) + displayTemperatureUnits());
            } else {
                final String sRangeString = mFormatter.format(dMin) + SEPARATOR + mFormatter.format(dMax) + displayTemperatureUnits();
                columnData.add(sRangeString);
            }

            //supplementary column data - not currently displayed
            columnData.add(String.valueOf(item.pressure));
            columnData.add(String.valueOf(item.humidity));
            columnData.add(String.valueOf(item.windSpeed));
            columnData.add(String.valueOf(item.windDirection));
            columnData.add(String.valueOf(item.weatherIcon));

            rowsToDisplay.add(columnData);
        }
        return rowsToDisplay;
    }

    /**
     * Utility function : returns the kelvin temperature in the preferred units
     * @param kelvinTemp temperature in Kelvin
     * @return temperature in the preferred units (from shared preferences
     */
    private double convertTemp(double kelvinTemp) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        final String sUnits = preferences.getString(mContext.getString(R.string.pref_key_temperature_units), UNITS_METRIC).trim();
        if (sUnits.equals(UNITS_METRIC)) {
            return kelvinTemp - 273.15;
        } else if (sUnits.equals(UNITS_IMPERIAL)) {
            return (kelvinTemp * 9/5) - 459.67;
        } else {
            return kelvinTemp;
        }
    }

    /**
     * Serves a request to read persisted weather data from the persisted database. Calls back to
     * requester on completion
     * @param callback callback reference to the requester
     */
    @Override
    public void readPersistedWeatherFromDB(IDbResponse callback) {
        List<WeatherData> weatherData;
        try {
             weatherData = readPersistedWeather();
        } catch (Exception e) {
            callback.onDbFailure(997, mContext.getString(R.string.error_mess_db_failure) + e.getMessage());
            return;
        }
        if (weatherData.size()==0) {
            callback.onDbEmptyData(mContext.getString(R.string.error_mess_db_no_weather_data));
        } else {
            callback.onDbSuccessfulRead(formatWeatherDisplayDataFromDatabase(weatherData));
        }
    }

    /**
     * Persist Weather data returned from the API into the WeatherPersistenceDatabase
     * @param cityList data from API
     */
    private void persistApiWeather(List<CityList> cityList) {
        WeatherPersistenceDatabase dBase =  WeatherPersistenceDatabase.getDatabase(mContext);
        try {
            dBase.weatherDao().removeAllWeather();
        } catch (Exception e) {
            Log.d(TAG, mContext.getString(R.string.error_mess_db_cannot_clear) + e.getLocalizedMessage());
            return;
        }
        for (CityList cityItem : cityList) {
            WeatherData table = new WeatherData(cityItem.getId(),
                    cityItem.getName(),
                    cityItem.getWeather().get(0).getDescription(),
                    cityItem.getMain().getTempMax(),
                    cityItem.getMain().getTempMin(),
                    //supplementary column data - not currently displayed
                    cityItem.getMain().getPressure(),
                    cityItem.getMain().getHumidity(),
                    cityItem.getWind().getSpeed(),
                    cityItem.getWind().getDeg(),
                    cityItem.getWeather().get(0).getIcon());

            try {
                dBase.weatherDao().addWeather(table);
            } catch (Exception e) {
                Log.d(TAG, mContext.getString(R.string.error_mess_db_cannot_write) + e.getLocalizedMessage());
            }
        }
    }
    /**
     * GET Weather data persisted on the WeatherPersistenceDatabase
     * @Returns List of WeatherData items
     */
    private List<WeatherData> readPersistedWeather() {
        List<WeatherData> weatherList = new ArrayList<>();
        WeatherPersistenceDatabase dBase =  WeatherPersistenceDatabase.getDatabase(mContext);
        try {
            weatherList = dBase.weatherDao().getAllWeather();
            return weatherList;
        } catch (Exception e) {
            Log.d(TAG, mContext.getString(R.string.error_mess_db_read_exception) + e.getLocalizedMessage());
            throw e;
        }
    }


    /**
     * Return a temperature units string based on the temperature units shared preference
     * @return Temperature units string
     */
    private String displayTemperatureUnits() {
        final String DEGREES = "\u00B0";  // Degrees symbol
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        final String sUnits = preferences.getString(mContext.getString(R.string.pref_key_temperature_units),
                mContext.getString(R.string.pref_temperature_units_default)).trim();
        switch (sUnits) {
            case "metric":
                return DEGREES + "C";
            case "imperial":
                return DEGREES + "F";
            default:
                return "K";

        }

    }
}
