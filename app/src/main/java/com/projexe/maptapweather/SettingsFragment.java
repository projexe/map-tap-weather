package com.projexe.maptapweather;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;


/**
 **Basic settings fragment implements preferences defined in xml/pref_general
 * @author Simon Hutton
 * @version 1.0
 */
public class SettingsFragment extends PreferenceFragment implements ISettingsContract.View {

    private static String TAG = "SettingsFragment";
    private ISettingsContract.PresenterListener mPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_general);

        mPresenter = new SettingsPresenter(this);

        EditTextPreference citiesPreference = (EditTextPreference) getPreferenceScreen().findPreference(getString(R.string.pref_key_number_of_cities));

        citiesPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return mPresenter.validateNumLocations(Long.valueOf(newValue.toString()));
            }
        });

    }

    /**
     * Implementation of interface callback for displaying a max locations error in a Toast
     * @param maxLocations the max number of locations allowed
     */
    @Override
    public void displayMaxLocationsError(long maxLocations) {
        Toast.makeText(getActivity(), getString(R.string.preference_error_max_locations_prefix)
                + maxLocations + getString(R.string.preference_error_max_locations_postfix),Toast.LENGTH_LONG)
                .show();
    }
}
