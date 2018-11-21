package com.foursquare.Presenter;


import com.foursquare.Model.History.MHistory;

import java.util.ArrayList;
/*
 * History Presenter
 * Created By Deepika Bhandari - 20 Nov 2018
 */

public class HistoryPresenter {
    private ArrayList<String> mArrayListKeywords = new ArrayList<>();
    private MHistory mMHistory;

    public HistoryPresenter() {
        mMHistory = new MHistory();
        mArrayListKeywords.clear();
        mArrayListKeywords.addAll(mMHistory.getKeywords(null));
    }

    public ArrayList<String> getHistory() {
        return mArrayListKeywords;
    }

    public void refreshHistory(String mKeyword) {
        mArrayListKeywords.clear();
        mArrayListKeywords.addAll(mMHistory.getKeywords(mKeyword));
    }

    public void saveHistory(String mKeyword) {
        mMHistory.saveHistory(mKeyword);
    }

}
