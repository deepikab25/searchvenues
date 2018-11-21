package com.foursquare.Service;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.foursquare.Core.NetworkManager;
import com.foursquare.Core.Utility;
import com.foursquare.Model.Search.VenueResponseDTO;
import com.foursquare.Presenter.HomeScreenPresenter;
import com.foursquare.R;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
/*
 * AsyncTask
 * Created By Deepika Bhandari - 20 Nov 2018
 */

public class MSearchAsynchTask extends AsyncTask<Bundle, Integer, Boolean> {

    private Context mContext;
	String mServerPath = Utility.getPathEndpointSearch();
    private HomeScreenPresenter mHomeScreenPresenter;


	public MSearchAsynchTask(Context _mContext, HomeScreenPresenter _mHomeScreenPresenter)
	{
	    mContext = _mContext;
        mHomeScreenPresenter = _mHomeScreenPresenter;
        mServerPath += "?v=" + Utility.getVersion() + "&client_id=" + Utility.getClientId()
                + "&client_secret=" + Utility.getClientSecret();

	}
    /**
     * Function actual network process takes place
     * */
	protected Boolean doInBackground(Bundle... arg0) 
	{
        VenueResponseDTO mVenueResponseDTO = null;
        try
        {
            if(NetworkManager.checkInternet(mContext))
            {
                 if(arg0 != null && arg0.length>0)
                {
                    Bundle mBundle = arg0[0];
                    if((mBundle != null) && (mBundle.size() > 0))
                    {
                        mServerPath += "&ll=" + mBundle.getString("lat_long");
                        mServerPath += "&query=" + URLEncoder.encode(mBundle.getString("query"),"UTF-8");
                        Log.d("lat_long_path", mServerPath);
                    }
                }

                InputStream mInputStream = NetworkManager.inputStreamPostData(mServerPath);

                if(mInputStream != null)
                {
                    Gson gson = new Gson();
                    Reader reader = new InputStreamReader(mInputStream);
                    mVenueResponseDTO = gson.fromJson(reader, VenueResponseDTO.class);
                }else{
                    Toast.makeText(mContext,Utility.getDisplayText(R.string.no_response_received),Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(mContext,Utility.getDisplayText(R.string.nointernet),Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Utility.log(e.getLocalizedMessage());
            mVenueResponseDTO = null;
        }
        if(mHomeScreenPresenter != null)
        {
            mHomeScreenPresenter.updateView(mVenueResponseDTO);
        }
        return true;
	}

}
