package com.foursquare.Model.Search;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/*
 * Location DTO
 * Created By Deepika Bhandari - 20 Nov 2018
 */
public class LocationDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @SerializedName("address")
    private String address;
    @SerializedName("distance")
    private String distance;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    @SerializedName("country")
    private String country;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
