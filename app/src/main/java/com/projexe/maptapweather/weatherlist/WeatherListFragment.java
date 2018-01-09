package com.projexe.maptapweather.weatherlist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.projexe.maptapweather.R;
import com.projexe.maptapweather.model.utils.Injection;
import com.projexe.maptapweather.model.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for displaying a list of weather data.
 * Implements interface for communicating with presenter which controls the presentation of the data
 */
public class WeatherListFragment extends Fragment implements IWeatherListContract.View,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private View mFragmentView;
    private ViewSwitcher mSwitcher;
    private TextView mEmptyStateMessageTv;
    private WeatherListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    //private IWeatherService mService;
    private IWeatherListContract.PresenterListener mPresenter;
    private ConstraintLayout mWeatherLayout;
    private ConstraintSet mEmptySetCs = new ConstraintSet();
    private ConstraintSet mListSetCs = new ConstraintSet();

    public WeatherListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.weather_display_fragment, container, false);

        // Obtain the SupportMapFragment and callback to onMapReady when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ///mService = ApiUtils.getWeatherService();
        mRecyclerView = mFragmentView.findViewById(R.id.rv_weather);

        //set up the view switcher and initialise
        mSwitcher = mFragmentView.findViewById(R.id.viewSwitcher);
        mEmptyStateMessageTv = (TextView) mFragmentView.findViewById(R.id.empty_state_message);
        if (mSwitcher.getNextView().getId() == R.id.rv_weather) {
            mSwitcher.showNext();
        }

        // Initialise constraint sets for transitions
        mWeatherLayout = mFragmentView.findViewById(R.id.weather_display_layout);
        mEmptySetCs.clone(mWeatherLayout);
        mListSetCs.clone(mWeatherLayout);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // When portrait, layout will transition from 55:45 to 90:10 depending on whether list is empty
            mListSetCs.setGuidelinePercent(R.id.weather_list_guideline, 0.55f);
            mEmptySetCs.setGuidelinePercent(R.id.weather_list_guideline, 0.90f);
        } else {
            // When landscape, layout will transition from 45:55 to 65:35 depending on whether list is empty
            mListSetCs.setGuidelinePercent(R.id.weather_list_guideline, 0.45f);
            mEmptySetCs.setGuidelinePercent(R.id.weather_list_guideline, 0.65f);
        }

        // TODO: Future development - inject this dependency
        mAdapter = new WeatherListAdapter(new ArrayList<List>(0), new WeatherListAdapter.WeatherItemListener() {
            @Override
            public void onWeatherClick(String mess) {
                // Handler for tapping on weather list. Currently unused. Could be used for later iterations where
                // a tap of a weather item will display more data.
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        // Initialise the Presenter.
        // mAskOdometerPresenter is used to call methods on the Presenter. Pass the constructor a reference
        // to this and inject a reference to a new Bike Repository (the Model layer)
        // The model will hold a reference to the context in order to get the preferences

        mPresenter = new WeatherListPresenter(this, Injection.provideModel(getActivity().getApplicationContext()));

        mPresenter.fetchPersistedWeather();

        return mFragmentView;
    }

    @Override
    public void displayWeatherData(List<List> displayList, boolean staleData) {
        TransitionManager.beginDelayedTransition(mWeatherLayout);
        mListSetCs.applyTo(mWeatherLayout);

        if (mSwitcher.getNextView().getId() == R.id.rv_weather) {
            // switch the View to the weather recyclerview
            mSwitcher.showNext();
        }
        mAdapter.updateWeather(displayList, staleData);
    }

    @Override
    public void displayToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayMaterialDesignEmptyState(String message) {
        TransitionManager.beginDelayedTransition(mWeatherLayout);
        mEmptySetCs.applyTo(mWeatherLayout);

        mEmptyStateMessageTv.setText(message);
        if (mSwitcher.getNextView().getId() == R.id.empty_state) {
            // switch the View to the empty state view
            mSwitcher.showNext();
        }

    }

     /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        enableMyLocation();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.fetchPersistedWeather();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        final int LOCATION_PERMISSION_REQUEST_CODE = 1;
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mPresenter.fetchWeather(latLng.latitude, latLng.longitude);

    }
}
