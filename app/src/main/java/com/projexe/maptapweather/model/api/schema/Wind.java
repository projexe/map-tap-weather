package com.projexe.maptapweather.model.api.schema;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Defines "wind" speed and direction data.
 * Schema model class used for mapping Java fields to JSON keys returned from the
 * the Open Weather Map API (https://openweathermap.org/current)
 * Note : Unused getters and setters can be deleted on app publication but remain
 * for development
 * @author  Simon Hutton
 * @version 1.0
 */
public class Wind {

    @SerializedName("speed")
    @Expose
    private double speed;
    @SerializedName("deg")
    @Expose
    private double deg;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

}
