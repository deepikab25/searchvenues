package com.foursquare.Model.Search;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/*
 * Venue DTO
 * Created By Deepika Bhandari - 20 Nov 2018
 */
public class VenueDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @SerializedName("name")
    private String name;

    private LocationDTO location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }
}
