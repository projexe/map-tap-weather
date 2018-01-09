package com.projexe.maptapweather.model.repository;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * "Room" Entity class defining a data table for persisting Weather data
 * @author  Simon Hutton
 * @version 1.0
 *
 */

@Entity
public class WeatherData {

    @PrimaryKey
    @ColumnInfo(name = "_id") // underscore prefic used to be mandatory when using SQLLiteAssetHelpers - old habits...
    public final int id;
    public String cityName;
    public String description;
    public double maxTemp;
    public double minTemp;
    public double pressure;
    public double humidity;
    public double windSpeed;
    public double windDirection;
    public String weatherIcon;

    public WeatherData(int id, String cityName, String description, double maxTemp,
                       double minTemp, double pressure, double humidity, double windSpeed,
                       double windDirection, String weatherIcon) {
        this.id = id;
        this.cityName = cityName;
        this.description  = description;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.weatherIcon = weatherIcon;
    }
}
