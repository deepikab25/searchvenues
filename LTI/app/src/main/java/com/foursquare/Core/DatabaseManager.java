package com.foursquare.Core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.foursquare.R;

/*
 * Database Manager
 * Created By Deepika Bhandari - 19 Nov 2018
 * Contains all the database operations
 */
public class DatabaseManager extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "foursquare.db";
	private static final int DATABASE_VERSION = 1;


	private static DatabaseManager mDatabaseManager = null;
	private final SQLiteDatabase mSQLdb;

	/*
	 * Returns writable database
	 */
	private DatabaseManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		mSQLdb = getWritableDatabase();
        mSQLdb.setForeignKeyConstraintsEnabled(true);
        mSQLdb.execSQL("PRAGMA foreign_keys=ON;");
	}

	public static synchronized DatabaseManager getInstance(Context context) {
		if (mDatabaseManager == null) {
			mDatabaseManager = new DatabaseManager(context.getApplicationContext());
		}
		return mDatabaseManager;
	}
	/*
	 * Create database and table if not exists
	 */
    public static void buildSnapshot(Context mContext) {
        DatabaseManager mTempDatabaseManager = getInstance(mContext);
        String [] mSQL = mContext.getResources().getStringArray(R.array.createsqlqueries);
		for (String query:mSQL) {
			mTempDatabaseManager.createTable(query);
		}
    }
	/*
	 * Create table operation
	 */
	private void createTable(String sql) {
		synchronized (mSQLdb) {
			mSQLdb.execSQL(sql);
		}

	}
	/*
	 * Insert in table operation
	 */
	public void insertOperation(String table_name, ContentValues cv) {
		synchronized (mSQLdb) {
			mSQLdb.insertWithOnConflict(table_name, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
		}

	}
	/*
	 * Select from table operation based on the query provided in the parameter
	 */
	public Cursor selectOperation(String sql) {
		synchronized (mSQLdb) {
			int count = 0;
			Cursor result_cursor;
			result_cursor = mSQLdb.rawQuery(sql, null);
			if(result_cursor == null) {
				while(count < 2) {
					result_cursor = mSQLdb.rawQuery(sql, null);
					if(result_cursor != null) {
						return result_cursor;
					}
					count ++;
				}
			}
			return result_cursor ;
		}
	}
	/*
	 * Close the database connection
	 */
	public void closeDB() {
		synchronized (mSQLdb) {
			mSQLdb.close();
		}
	}


	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

	}
}
