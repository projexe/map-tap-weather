package com.projexe.maptapweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;


/**
 **Basic Settings activity for managing app preferences
 * @author Simon Hutton
 * @version 1.0
 */
public class SettingsActivity extends AppCompatActivity {

    public final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false); // Required to initialise preference defaults

    }

}
