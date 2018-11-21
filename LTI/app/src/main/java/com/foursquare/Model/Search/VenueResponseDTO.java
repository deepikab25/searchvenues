package com.foursquare.Model.Search;

import java.io.Serializable;
/*
 * VenueResponse DTO
 * Created By Deepika Bhandari - 20 Nov 2018
 */
public class VenueResponseDTO implements Serializable {

    private static final long serialVersionUID=1L;

    private MetaDTO meta;
    private ResponseDTO response;

    public MetaDTO getMeta() {
        return meta;
    }

    public void setMeta(MetaDTO meta) {
        this.meta = meta;
    }

    public ResponseDTO getResponse() {
        return response;
    }

    public void setResponse(ResponseDTO response) {
        this.response = response;
    }
}

