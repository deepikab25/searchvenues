package com.foursquare.Model.Search;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/*
 * Meta DTO
 * Created By Deepika Bhandari - 20 Nov 2018
 */
public class MetaDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @SerializedName("code")
    private String code;
    @SerializedName("requestId")
    private String requestId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
