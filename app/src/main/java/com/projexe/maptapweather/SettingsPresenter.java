package com.projexe.maptapweather;

/**
 ** Settings presenter for handling presentation business, specifically validation
 * @author Simon Hutton
 * @version 1.0
 */
public class SettingsPresenter implements ISettingsContract.PresenterListener {

    private static String TAG = "SettingsPresenter";
    private final long MAX_LOCATIONS = 15;
    private ISettingsContract.View mView;

    SettingsPresenter(ISettingsContract.View view) {
        mView = view;
    }

    /**
     * Validate number of locations
     * @param locations number of locations
     * @return true is <= MAX_LOCATIONS
     */
    @Override
    public boolean validateNumLocations(long locations) {
        boolean valid = locations <= MAX_LOCATIONS;
        if (!valid) mView.displayMaxLocationsError(MAX_LOCATIONS);
        return valid;
    }
}
