package com.projexe.maptapweather.weatherlist;

import java.util.List;

/**
 * Contract for the WeatherList package MVP
 *
 * interface View : the interface to the View layer. Fragment will implement this interface.
 * In the Fragment the Presenter is instantiated and passed a reference to the Fragment.
 * All callbacks to the Fragment from the Presenter are made using the interface
 *
 * interface UserActionListener : interface implemented by the Presenter to enable calls to be made
 * from the View to the Presenter.
 *
 * @author Simon Hutton
 * @version 1.0
 * */

public interface IWeatherListContract {

    interface View {

        void displayWeatherData(List<List> displayList, boolean bStaleData);

        void displayToast(String message);

        void displayMaterialDesignEmptyState(String message);

    }

    interface PresenterListener {

        void fetchPersistedWeather();

        void fetchWeather(double lat, double lon);
    }
}
