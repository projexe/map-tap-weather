/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.projexe.maptapweather;

import com.projexe.maptapweather.weatherlist.WeatherListPresenter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;


/**
 * Unit tests for the implementation of {@link WeatherListPresenter}.
 */
public class SettingsPresenterTest {

    private SettingsPresenter mSettingsPresenter;

    @Mock
    private ISettingsContract.View mViewInterface;

    @Before
    public void setupWeatherListPresenter() {
        // inject annotated mocks
        MockitoAnnotations.initMocks(this);
        // Get a reference to the test class with the mock interfaces
        mSettingsPresenter = new SettingsPresenter(mViewInterface);
    }

    @Test
    public void ValidateLocations_OverLimit() {
        // When the presenter is asked to fetch persisted weather
        boolean result = mSettingsPresenter.validateNumLocations(16);
        // Call model to fetch persisted weather
        verify(mViewInterface).displayMaxLocationsError(15);
        Assert.assertEquals(result, false);
   }

    @Test
    public void ValidateLocations_WithinLimit() {
        // When the presenter is asked to fetch persisted weather
        boolean result = mSettingsPresenter.validateNumLocations(15);
        // Call model to fetch persisted weather
        Assert.assertEquals(result, true);
    }

}
