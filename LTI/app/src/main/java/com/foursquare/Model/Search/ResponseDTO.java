package com.foursquare.Model.Search;

import java.io.Serializable;
import java.util.List;
/*
 * Response DTO
 * Created By Deepika Bhandari - 20 Nov 2018
 */
public class ResponseDTO implements Serializable {

    private static final long serialVersionUID=1L;

    private List<VenueDTO> venues;

    public List<VenueDTO> getVenues() {
        return venues;
    }
}
