package com.projexe.maptapweather.model.api.schema;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Schema model class used for mapping Java fields to JSON keys returned from the
 * the Open Weather Map API (https://openweathermap.org/current)
 * This class defines top level JSON
 * Note : Unused getters and setters can be deleted on app publication but remain
 * for development
 * @author  Simon Hutton
 * @version 1.0
 */
public class CitiesInCycle {

    @SerializedName("list") // maps JSON "list" to cityList (etc)
    @Expose
    private java.util.List<CityList> cityList = null;
    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public java.util.List<CityList> getCityList() {
        return cityList;
    }

    public void setCityList(java.util.List<CityList> cityList) {
        this.cityList = cityList;
    }

}
