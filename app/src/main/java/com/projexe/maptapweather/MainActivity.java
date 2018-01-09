package com.projexe.maptapweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 **App Main Activity
 * @author Simon Hutton
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    public final String TAG = "MainActivity";

    /**
     * Create main activity
     * @param savedInstanceState saved instance data bumdle
     */
    @Override
    protected void onCreate (Bundle savedInstanceState)  {
        super.onCreate( savedInstanceState );
        //Start crashlytics
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main );
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Inflate the menu
     * @param menu Activity menu interface
     * @return true if menu created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Options selected menu handler. Action bar will automatically handle clicks on the Home/Up
     * button, so long as parent activity specified in AndroidManifest.xml.
     * @param item Item selected
     * @return true if item handles
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // Display settings activity
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
