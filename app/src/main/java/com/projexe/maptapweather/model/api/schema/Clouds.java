package com.projexe.maptapweather.model.api.schema;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Defines "clouds" data.
 * Schema model class used for mapping Java fields to JSON keys returned from the
 * the Open Weather Map API (https://openweathermap.org/current)
 * Note : Unused getters and setters can be deleted on app publication but remain
 * for development
 * @author  Simon Hutton
 * @version 1.0
 */
public class Clouds {

    @SerializedName("all")
    @Expose
    private double all;

    public double getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }

}
