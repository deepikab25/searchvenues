package com.foursquare.Core;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

public class DatabaseManagerTest {

    Context mContext =  mock(Context.class);
    @Mock
    SQLiteDatabase mSQLdb;


    @Test
    public void insertOperation() {


    }

    @Test
    public void selectOperation() {
    }

    @Test
    public void buildSnapshot() {
        DatabaseManager databaseManager = mock(DatabaseManager.class);
        String tableQuery = "select * from history";
        Cursor cursor = databaseManager.selectOperation(tableQuery);
        assertEquals(null,cursor);


    }

    @Test
    public void insertOperation1() {
    }

    @Test
    public void selectOperation1() {
    }
}