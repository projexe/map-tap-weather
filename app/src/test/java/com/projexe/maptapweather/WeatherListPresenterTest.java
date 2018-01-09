package com.projexe.maptapweather;

import com.projexe.maptapweather.model.IModelContract;
import com.projexe.maptapweather.model.repository.WeatherData;
import com.projexe.maptapweather.weatherlist.IWeatherListContract;
import com.projexe.maptapweather.weatherlist.WeatherListPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;


/**
 * Unit tests for the implementation of {@link com.projexe.maptapweather.weatherlist.WeatherListPresenter}.
 * @author Simon Hutton
 * @version 1.0
 */
public class WeatherListPresenterTest {

    private WeatherListPresenter mWeatherListPresenter;

    @Mock
    private IModelContract mModelInterface;
    @Mock
    private IWeatherListContract.View mViewInterface;
    @Mock
    private List<WeatherData> mWeatherData;
    @Mock
    private List<List> mDisplayList;

    // Create argument captors for callbacks
    @Captor
    private ArgumentCaptor<IModelContract.IApiResponse> mModelAPICallbackCaptor;
    @Captor
    private ArgumentCaptor<IModelContract.IDbResponse> mModelDBCallbackCaptor;

    @Before
    public void setupWeatherListPresenter() {
        // inject annotated mocks
        MockitoAnnotations.initMocks(this);
        // Get a reference to the test class with the mock interfaces
        mWeatherListPresenter = new WeatherListPresenter(mViewInterface, mModelInterface);
    }

    @Test
    public void fetchPersistedData_CallsModelToReadDB() {
        String message = "No weather data to show";
        // When the presenter is asked to fetch persisted weather
        mWeatherListPresenter.fetchPersistedWeather();
        // Call model to fetch persisted weather
        Mockito.verify(mModelInterface).readPersistedWeatherFromDB(any(IModelContract.IDbResponse.class));
        Mockito.verify(mModelInterface).readPersistedWeatherFromDB(mModelDBCallbackCaptor.capture());

        //TEST THE 3 AVAILABLE CALLBACKS
        //If callback is Empty Data
        mModelDBCallbackCaptor.getValue().onDbEmptyData(message);
        // Display Empty State
        Mockito.verify(mViewInterface).displayMaterialDesignEmptyState(message);

        // If callback is Successful Read
        mModelDBCallbackCaptor.getValue().onDbSuccessfulRead(mDisplayList);
        // Display data
        Mockito.verify(mViewInterface).displayWeatherData(mDisplayList, true);

        mModelDBCallbackCaptor.getValue().onDbFailure(999, message);
        // Display FAIL data on view
        Mockito.verify(mViewInterface).displayToast(message);
    }

    @Test
    public void fetchWeatherFromAPI_displayWeather() {
        double lat = 12.3d;
        double lon = 45.6d;
        String message = "Anyold message";
        // Ask to fetch weather from API
        mWeatherListPresenter.fetchWeather(lat,lon);
        // display weather on view
        Mockito.verify(mModelInterface).fetchWeatherFromApi(any(IModelContract.IApiResponse.class),eq(lat),eq(lon));
        Mockito.verify(mModelInterface).fetchWeatherFromApi(mModelAPICallbackCaptor.capture(),eq(lat),eq(lon));

        //TEST THE 4 AVAILABLE CALLBACKS
        mModelAPICallbackCaptor.getValue().onApiEmptyData(message);
        // Ask view to display empty dataset
        Mockito.verify(mViewInterface).displayMaterialDesignEmptyState(message);

        // Successful API read
        mModelAPICallbackCaptor.getValue().onApiSuccessReceived(mDisplayList);
        // Ask view to display empty dataset
        Mockito.verify(mViewInterface).displayWeatherData(mDisplayList,false);

        // Receive failure from Model
        mModelAPICallbackCaptor.getValue().onApiFailureReceived(5, message);
        // sent failure to View
        Mockito.verify(mViewInterface).displayToast(message);

        // Receive failure from Model
        mModelAPICallbackCaptor.getValue().onApiOfflinePersistedData(mDisplayList);
        // Display persisted data
        Mockito.verify(mViewInterface).displayWeatherData(mDisplayList, true);
    }

    @Test
    public void receivedEmptyData_displayEmptyData() {
        // When the presenter has received an empty dataset
        String messageToDisplay = "Any response message";
        mWeatherListPresenter.onApiEmptyData(messageToDisplay);
        // Ask view to display empty dataset
        verify(mViewInterface).displayMaterialDesignEmptyState(messageToDisplay);
    }
    @Test

    public void receivedValidData_displayWeatherList() {
        // When the presenter has received an empty dataset
        List<List> displayData = new ArrayList<List>();
        mWeatherListPresenter.onApiSuccessReceived(displayData);
        // Ask view to display empty dataset
        verify(mViewInterface).displayWeatherData(displayData,false);
    }


    @Test
    public void receivedFailureFromAPI_DisplayFailMessage() {
        String message ="any message";
        // Receive failure from Model
        mWeatherListPresenter.onApiFailureReceived(5, message);
        // sent failure to View
        verify(mViewInterface).displayToast(message);
    }

    @Test
    public void receivedOfflineFailureFromAPI_DisplayPersistedData() {
        // Receive failure from Model
        mWeatherListPresenter.onApiOfflinePersistedData(mDisplayList);
        // Display persisted data
        verify(mViewInterface).displayWeatherData(mDisplayList, true);
    }

    @Test
    public void receivedPersistedDBData_DisplayDBPersistedData() {
        // Receive persisted data deom database
        mWeatherListPresenter.onDbSuccessfulRead(mDisplayList);
        // Display data on view
        verify(mViewInterface).displayWeatherData(mDisplayList, true);
    }

    @Test
    public void receivedPersistedDBDataEmpty_DisplayEmptyData() {
        String message ="any message";
        // Receive persisted data deom database
        mWeatherListPresenter.onDbEmptyData(message);
        // Dont display data
        verify(mViewInterface).displayMaterialDesignEmptyState(message);
    }

    @Test
    public void receivedPersistedDBFailure_DisplayFailMessage() {
        String message ="any message";
        // Receive persisted data deom database
        mWeatherListPresenter.onDbFailure(9, message);
        // Display data on view
        verify(mViewInterface).displayToast(message);
    }
}
