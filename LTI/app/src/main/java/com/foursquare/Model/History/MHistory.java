package com.foursquare.Model.History;


import android.content.ContentValues;
import android.database.Cursor;

import com.foursquare.Core.BaseApplication;
import com.foursquare.Core.Constants;
import com.foursquare.Core.Utility;

import java.util.ArrayList;
/*
 * Network Manager
 * Created By Deepika Bhandari - 19 Nov 2018
 */
public class MHistory {

    private ContentValues mContentValues = new ContentValues();
    private ArrayList<String> mArrayListKeywords = new ArrayList<>();

    public MHistory() {
        mArrayListKeywords.clear();
    }

    /**
     * Function to get keywords entered by the user
     * */
    public ArrayList<String> getKeywords(String mKeyword) {
        refreshKeywords(mKeyword);
        return mArrayListKeywords;
    }

    /**
     * Function to get the refresh keywords list from database
     * */
    private void refreshKeywords(String mKeyword) {
        mArrayListKeywords.clear();
        String mSQL =Constants.selectQuery ;

        if (Utility.validateString(mKeyword)) {
            mSQL = Constants.selectQuerySearch +mKeyword.trim().toLowerCase()+"%';";
        }

        Cursor mCursor = BaseApplication.getDatabaseManager().selectOperation(mSQL);

        if (mCursor!=null && mCursor.getCount()>0) {
            mCursor.moveToFirst();
            while (mCursor.moveToNext()) {
                mArrayListKeywords.add(mCursor.getString(mCursor.getColumnIndex(Constants.keywordsearched)));
            }
        }
        if (mCursor != null)
            mCursor.close();
    }

    /**
     * Function to save the keyword typed in the searchbar and the on keyboard search button is pressed
     * */
    public void saveHistory(String mKeyword) {
        try {
            mContentValues.clear();
            if(!isAlreadyPresent(mKeyword)) {
                mContentValues.put(Constants.keywordsearched, mKeyword.trim().toLowerCase());
                BaseApplication.getDatabaseManager().insertOperation(Constants.tableName, mContentValues);
            }
        }
        catch (Exception e) {
            mContentValues.clear();
            Utility.log(e.getLocalizedMessage());
        }
    }
    /**
     * Function to check whether the keyword is present in the database before adding
     * */
    private boolean isAlreadyPresent(String mKeyword) {
        boolean flag = false;

        mKeyword = mKeyword.trim().toLowerCase();
        String mSQL = Constants.selectQueryKeywordSearch+"'"+mKeyword+"';";

        Cursor mCursor = BaseApplication.getDatabaseManager().selectOperation(mSQL);

        if (mCursor!=null && mCursor.getCount() > 0) {
            flag = true;
        }

        if (mCursor != null) {
            mCursor.close();
        }

        return flag;
    }
}
