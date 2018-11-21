package com.foursquare.Service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.foursquare.Core.Utility;
import com.foursquare.R;

/*
 * GPS Manager
 * Created By Deepika Bhandari - 19 Nov 2018
 */
public class GPSManager extends Service implements LocationListener
{

    Context mContext;

    // Flag for GPS status
    boolean isGPSEnabled = false;

    // Flag for network status
    boolean isNetworkEnabled = false;

    // Flag for GPS status
    boolean canGetLocation = false;

    Location mLocation; // Location
    double mLatitude; // Latitude
    double mLongitude; // Longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100; // 100 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute

    protected LocationManager mLocationManager;

    public GPSManager(Context context) {
        this.mContext = context;
        getLocation();
    }

    public void getLocation() {
        try {
            mLocationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // Getting GPS status
            isGPSEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            isNetworkEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (isGPSEnabled && isNetworkEnabled) {
                this.canGetLocation = true;
                    setCordinates(mLocationManager,LocationManager.NETWORK_PROVIDER);
                // If GPS enabled, get latitude/longitude using GPS Services
                    if (mLocation == null) {
                        setCordinates(mLocationManager,LocationManager.GPS_PROVIDER);
                    }
            }else{
                Toast.makeText(mContext,Utility.getDisplayText(R.string.nointernet),Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Utility.log(e.getLocalizedMessage());
        }


    }



    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app.
     * */
    public void stopUsingGPS(){
        if(mLocationManager != null){
            mLocationManager.removeUpdates(GPSManager.this);
        }
    }


    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(mLocation != null){
            mLatitude = mLocation.getLatitude();
        }

        // return latitude
        return mLatitude;
    }


    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(mLocation != null){
            mLongitude = mLocation.getLongitude();
        }

        // return longitude
        return mLongitude;
    }

    public boolean isGPSOnn()
    {
        mLocationManager = (LocationManager) mContext
                .getSystemService(LOCATION_SERVICE);

        // Getting GPS status
        isGPSEnabled = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Getting network status
        isNetworkEnabled = mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return (isGPSEnabled || isNetworkEnabled);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    public void setCordinates(LocationManager mLocationManager,String provider){
     mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
     mLocation = mLocationManager.getLastKnownLocation(provider);
     if (mLocation != null) {
             mLatitude = mLocation.getLatitude();
             mLongitude = mLocation.getLongitude();
             }

}
}
