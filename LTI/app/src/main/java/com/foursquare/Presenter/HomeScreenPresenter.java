package com.foursquare.Presenter;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.foursquare.Core.Constants;
import com.foursquare.Core.Utility;
import com.foursquare.Model.Search.VenueDTO;
import com.foursquare.Model.Search.VenueListDTO;
import com.foursquare.Model.Search.VenueResponseDTO;
import com.foursquare.Service.GPSManager;
import com.foursquare.Service.MSearchAsynchTask;

import java.util.ArrayList;
import java.util.List;

import static com.foursquare.Core.Constants.DEFAULT_LATITUDE;
import static com.foursquare.Core.Constants.DEFAULT_LONGITUDE;
import static com.foursquare.Core.Constants.ERROR_FAILED_TO_GET_COORDINATES;
import static com.foursquare.Core.Constants.ERROR_GPS_IS_OFF;
import static com.foursquare.Core.Constants.ERROR_INCORRECT_REQUEST_ID;
import static com.foursquare.Core.Constants.ERROR_NO_RESPONSE_FROM_SERVER;
import static com.foursquare.Core.Constants.ERROR_NO_VENUES;
import static com.foursquare.Core.Constants.SUCCESS;
/*
 * HomeScreen Presenter
 * Created By Deepika Bhandari - 19 Nov 2018
 */
public class HomeScreenPresenter
{
    private Context mContext;
    private Handler mHandler ;
    private MSearchAsynchTask mSearchAsynchTask;
    private Bundle mBundle = new Bundle();
    private ArrayList<VenueListDTO> mArrayListVenues = new ArrayList<>();
    private GPSManager mGPSManager;



    public HomeScreenPresenter(Context _mContext, Handler _mHandler, GPSManager _mGPSManager) {
        mContext = _mContext;
        mHandler = _mHandler;
        mSearchAsynchTask = new MSearchAsynchTask(mContext, this);
        mGPSManager = _mGPSManager;
    }

    /**
     * Function to initiate network call for searching the typed keywords
     * */
    public void conductSearch(String mText) {
         int mResponseCode = ERROR_NO_RESPONSE_FROM_SERVER;
         double mLatitude = DEFAULT_LATITUDE;
         double mLongitude = DEFAULT_LONGITUDE;
        if (mSearchAsynchTask != null) {
            mSearchAsynchTask.cancel(true);
        }

        mSearchAsynchTask = null;

        if (!Utility.validateString(mText)) {
        	updateView();
        	return;
        }

        mBundle.clear();
        mBundle.putString("query", mText);

        // Get the co-ordinates
        mLatitude = DEFAULT_LATITUDE;
        mLongitude = DEFAULT_LONGITUDE;

        if (!mGPSManager.isGPSOnn()) {
        	mResponseCode = ERROR_GPS_IS_OFF;
            updateView();
            return;
        }           
        	
        mLatitude = mGPSManager.getLatitude();
        mLongitude = mGPSManager.getLongitude();

        if (mLatitude != DEFAULT_LATITUDE && mLongitude != DEFAULT_LONGITUDE) {
            // add the lat long
            mBundle.putString(Constants.ll, ""+mLatitude + "," + mLongitude);
            mSearchAsynchTask = new MSearchAsynchTask(mContext, this);
            mSearchAsynchTask.execute(mBundle);
            return;
        }

        mResponseCode = ERROR_FAILED_TO_GET_COORDINATES;
        updateView();
        mGPSManager.getLocation();
    }
    /**
     * Function to update the view based on the search result obtained
     * */
    private void updateView() {
        mArrayListVenues.clear();
        Message mMessage = mHandler.obtainMessage();
        Bundle mBundle = new Bundle();
        mBundle.putInt(Constants.responseCode, Constants.mResponseCode);
        mBundle.putSerializable(Constants.venues, mArrayListVenues);
        mMessage.setData(mBundle);
        mHandler.sendMessage(mMessage);
    }
    /**
     * Function to update the view based on the search result obtained
     * */
    public void updateView(VenueResponseDTO mVenueResponse) {

        mArrayListVenues.clear();
        if (mVenueResponse.getMeta() == null) {     
            sendMessage(ERROR_NO_RESPONSE_FROM_SERVER);
            return;
        }
        
        if (!Utility.validateString(mVenueResponse.getMeta().getRequestId())) {
        	sendMessage(ERROR_INCORRECT_REQUEST_ID);
        	return;
        }
        
        if (mVenueResponse.getResponse() == null) {
        	sendMessage(ERROR_INCORRECT_REQUEST_ID);
        	return;
        }
        
        List<VenueDTO> mListVenues = mVenueResponse.getResponse().getVenues();
        if (mListVenues == null && mListVenues.size() == 0) {
        	sendMessage(ERROR_NO_VENUES);
        	return;
        }
            
        for (VenueDTO mVenue: mListVenues) {
            VenueListDTO mVenueListDTO = new VenueListDTO();
            mVenueListDTO.setName(mVenue.getName());
            mVenueListDTO.setDistance(mVenue.getLocation().getDistance() +Constants.meters);
            mVenueListDTO.setAddress(mVenue.getLocation().getAddress());
            mArrayListVenues.add(mVenueListDTO);
        }
        sendMessage(SUCCESS);       
    }


	private void sendMessage(int mResponseCode) {
		Message mMessage = mHandler.obtainMessage();
        Bundle mBundle = new Bundle();
        mBundle.putInt(Constants.responseCode, mResponseCode);
        mBundle.putSerializable(Constants.venues, mArrayListVenues);
        mMessage.setData(mBundle);
        mHandler.sendMessage(mMessage);
	}

}
