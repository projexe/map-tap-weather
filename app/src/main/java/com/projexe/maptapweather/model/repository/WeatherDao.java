package com.projexe.maptapweather.model.repository;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * "Room" Data Access Object class defining a data access methods for Weather data
 * @author  Simon Hutton
 * @version 1.0
 *
 */
@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addWeather(WeatherData weatherData);

    @Query("select * from weatherdata")
    List<WeatherData> getAllWeather();
    //Observable<List<WeatherData>> getAllWeather();

    @Query("delete from weatherdata")
    void removeAllWeather();
}