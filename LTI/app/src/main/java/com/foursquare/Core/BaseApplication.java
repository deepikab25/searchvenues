package com.foursquare.Core;

import android.app.Application;
import android.content.Context;

import com.foursquare.Service.GPSManager;

/**
 * Base application
 * Created By Deepika Bhandari - 19 Nov 2018
 * Provides the application context
 */

public class BaseApplication extends Application {

    // Tag name used for logging
    public static String TAG = null;
    // Application context
    static Context mContext = null;
    static GPSManager mGPSManager = null;

    @Override
    public void onCreate() {

        super.onCreate();
        if(mContext == null) {
            mContext = BaseApplication.this;
        }
        TAG = Utility.getTagName();
        mGPSManager = new GPSManager(mContext);
        getDatabaseManager().buildSnapshot(mContext);
        Utility.log("Application created");
    }


    public static DatabaseManager getDatabaseManager() {
        return DatabaseManager.getInstance(mContext);
    }


    public static GPSManager getGPSManager() {

    	if (mGPSManager == null) {
            mGPSManager = new GPSManager(mContext);
        }
        return mGPSManager;
    }

    /*
	 * Returns the base application context
	 */
    public static Context getBaseApplicationContext() {
        return mContext;
    }
}
