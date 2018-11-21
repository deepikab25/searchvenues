package com.foursquare.Model.Search;


import java.io.Serializable;
/*
 * VenueListDTO DTO
 * Created By Deepika Bhandari - 20 Nov 2018
 */
public class VenueListDTO implements Serializable {

    private static final long serialVersionUID=1L;

    private String name;
    private String distance;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



}
