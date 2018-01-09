package com.projexe.maptapweather.model.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.projexe.maptapweather.model.IModelContract;
import com.projexe.maptapweather.model.ModelImplementationMock;
import com.projexe.maptapweather.model.WeatherModels;

/**
 * Simple injection class for injecting a "WeatherModel" i.e. the business functionality class into
 * an app build.
 * This is the MOCK version and will build into the 'mock' build variant
 * Enables injection of ***MOCK*** implementations for {@link IModelContract} at compile time.
 * @author Simon Hutton
 * @version 1.0
 */
public class Injection {

    public static IModelContract provideModel(@NonNull Context ctx) {
        return WeatherModels.getInMemoryInstance(new ModelImplementationMock(ctx));
    }
}
