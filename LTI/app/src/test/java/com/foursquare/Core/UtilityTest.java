package com.foursquare.Core;

import android.content.Context;
import android.test.mock.MockContext;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class UtilityTest {

    Context mContext =  new MockContext();//new BaseApplication().getApplicationContext();


    @Test
    public void getPathEndpointSearch() {
        String result = Utility.getPathEndpointSearch();
        String expected = "https://api.foursquare.com/v2/venues/search";
        assertTrue(expected.equals(result));

    }

    @Test
    public void getVersion() {
    }

    @Test
    public void validateString() {
        String input = "Search text";
        assertTrue(Utility.validateString(input));
     }
}