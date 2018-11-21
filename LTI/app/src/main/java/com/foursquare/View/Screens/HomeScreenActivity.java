package com.foursquare.View.Screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.foursquare.Core.BaseApplication;
import com.foursquare.Core.Constants;
import com.foursquare.Core.Utility;
import com.foursquare.Model.Search.VenueListDTO;
import com.foursquare.Presenter.HistoryPresenter;
import com.foursquare.Presenter.HomeScreenPresenter;
import com.foursquare.Service.GPSManager;
import com.foursquare.View.Adapters.HistoryCursorAdapter;
import com.foursquare.View.Adapters.HomeScreenListViewAdapter;

import java.util.ArrayList;

import static com.foursquare.Core.Constants.ERROR_INCORRECT_REQUEST_ID;
import static com.foursquare.Core.Constants.ERROR_NO_DATA_FROM_SERVER;
import static com.foursquare.Core.Constants.ERROR_NO_RESPONSE_FROM_SERVER;
import static com.foursquare.R.id;
import static com.foursquare.R.layout;
import static com.foursquare.R.string;
/*
 * HomeScreen Activity
 * Created By Deepika Bhandari - 19 Nov 2018
 * Activity performs search for the venue
 */

public class HomeScreenActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener, SearchView.OnSuggestionListener
{

    SearchView mSearchView = null;
    HomeScreenPresenter mHomeScreenPresenter = null;
    HomeScreenListViewAdapter mHomeScreenListViewAdapter = null;
    GPSManager mGPSManager = BaseApplication.getGPSManager();
    HistoryPresenter mHistoryPresenter = new HistoryPresenter();

    ArrayList<String> mArrayListKeywordsTemp = new ArrayList<>();
    public static String[] COLUMNS = new String[]{"_id", "KEYWORD"};
    HistoryCursorAdapter mHistoryCursorAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_home_screen);
        RecyclerView mRecyclerView;
        mGPSManager.getLocation();

        mRecyclerView = (RecyclerView)findViewById(id.recylerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSearchView = (SearchView)findViewById(id.search_bar);
        mHomeScreenPresenter = new HomeScreenPresenter(HomeScreenActivity.this, mHandler, mGPSManager);

        mHomeScreenListViewAdapter = new HomeScreenListViewAdapter(HomeScreenActivity.this);
        mRecyclerView.setAdapter(mHomeScreenListViewAdapter);

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSuggestionListener(this);

        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setIconified(false);
            }
        });
        mSearchView.setOnCloseListener(HomeScreenActivity.this);


        mHistoryCursorAdapter
                = new HistoryCursorAdapter(this, layout.suggestion_item, null, COLUMNS, null, -1000);
        mSearchView.setSuggestionsAdapter(mHistoryCursorAdapter);


    }


    @Override
    public boolean onSuggestionSelect(int i) {
        return false;
    }

    /**
     * Function to get the item clicked on the suggestion list
     * */
    @Override
    public boolean onSuggestionClick(int i) {
        mSearchView.setQuery(mArrayListKeywordsTemp.get(i), true);
        return false;
    }

    private MatrixCursor getKeywordCursor(String mText) {
        mArrayListKeywordsTemp.clear();
        mHistoryPresenter.refreshHistory(mText);
        mArrayListKeywordsTemp = mHistoryPresenter.getHistory();
        MatrixCursor mMatrixCursor = new MatrixCursor(COLUMNS);
       int counter =0;
       for(String mSuggestion: mArrayListKeywordsTemp){
           String[] temp = new String[2];
           temp[0] = ""+counter;
           temp[1] = mSuggestion;

           mMatrixCursor.addRow(temp);
           counter++;
       }
        return mMatrixCursor;
    }

    @Override
    public boolean onClose() {
        mHomeScreenPresenter.conductSearch(" ");
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( !mGPSManager.isGPSOnn() ) {
            LaunchSettingsScreen();
        }
    }
    /**
     * Function to check the GPS settings
     * */
    void LaunchSettingsScreen() {
        if (  !mGPSManager.isGPSOnn() ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(Utility.getDisplayText(string.gpsoff))
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.dismiss();
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * Handler to process the response data
     * */
    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            Bundle mBundle = msg.getData();
            ArrayList<VenueListDTO> mArrayListVenues = (ArrayList<VenueListDTO>) mBundle.getSerializable(Constants.venues);
            int response_code = mBundle.getInt(Constants.responseCode);
            mHomeScreenListViewAdapter.refresh(mArrayListVenues);
            mHomeScreenListViewAdapter.notifyDataSetChanged();
            switch (response_code)
            {
                case ERROR_NO_DATA_FROM_SERVER :
                    ToastMessage(Utility.getDisplayText(string.no_data_received));
                    break;

                case ERROR_INCORRECT_REQUEST_ID :
                    ToastMessage(Utility.getDisplayText(string.server_error_try_later));
                    break;

                case ERROR_NO_RESPONSE_FROM_SERVER :
                    //ToastMessage(Utility.getDisplayText(R.string.no_response_received));
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * Function initiated on the search query submitted from the search bar
     * */
    @Override
    public boolean onQueryTextSubmit(String query) {
        if ( !mGPSManager.isGPSOnn()  ) {
            LaunchSettingsScreen();
        }
        else {
            if(Utility.validateString(query)) {
                query = query.trim();
                mHistoryPresenter.saveHistory(query);
                mHistoryPresenter.refreshHistory(query);
            }
            mHomeScreenPresenter.conductSearch(query);
        }

        return false;
    }

    /**
     * Function initiated on the search query change from the search bar
     * */
    @Override
    public boolean onQueryTextChange(String newText) {
        if ( !mGPSManager.isGPSOnn()  ) {
            LaunchSettingsScreen();
        }
        else {
            // update the suggestions adapter
            mHistoryCursorAdapter.changeCursor(getKeywordCursor(newText));
            mHistoryCursorAdapter.notifyDataSetChanged();

            mHomeScreenPresenter.conductSearch(newText);
        }
        if(newText != null && newText.length() == 0) {
            mHomeScreenPresenter.conductSearch(" ");
        }
        return false;
    }

    /**
     * Function initiated on Back button pressed on device.
     * */
    @Override
    public void onBackPressed() {
        BaseApplication.getDatabaseManager().closeDB();
        finish();
    }

    void ToastMessage(String mToastMessage) {
        Toast.makeText(HomeScreenActivity.this, mToastMessage,Toast.LENGTH_SHORT ).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGPSManager.stopUsingGPS();
    }
}
