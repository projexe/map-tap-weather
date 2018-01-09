package com.projexe.maptapweather.model.api.schema;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Defines "coord" co-ordinates data.
 * Schema model class used for mapping Java fields to JSON keys returned from the
 * the Open Weather Map API (https://openweathermap.org/current)
 * Note : Unused getters and setters can be deleted on app publication but remain
 * for development
 * @author  Simon Hutton
 * @version 1.0
 */
public class Coord {

    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lon")
    @Expose
    private double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

}
