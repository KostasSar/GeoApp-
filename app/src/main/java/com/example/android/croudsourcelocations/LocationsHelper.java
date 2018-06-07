package com.example.android.croudsourcelocations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kostas on 8/12/2017.
 */


//This class helps with the SQLite Database management.
public class LocationsHelper extends SQLiteOpenHelper {

    //Defining variable names to insert in Database.
    public static final String DB_NAME = "locations.db";
    public static final String TABLE_NAME = "location_table";
    public static final String COL_1 = "id";
    public static final String COL_2 = "userid";
    public static final String COL_3 = "longitude";
    public static final String COL_4 = "latitude";
    public static final String COL_5 = "dt";

    public LocationsHelper(Context context) {

        super(context, DB_NAME, null, 1);
    }

    //Executes the initial query to create the table we are going to use in the database.
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Table creation query
        db.execSQL("create table " + TABLE_NAME + " ("  + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                        + COL_2 + " VARCHAR(10), "
                                                        + COL_3 + " FLOAT, "
                                                        + COL_4 + " FLOAT, "
                                                        + COL_5 + " VARCHAR(20)"
                                                        + " )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //This method inserts the information to the Db
    public boolean insertLocation(String userId,float longitude, float latitude, String dt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, userId);
        contentValues.put(COL_3, longitude);
        contentValues.put(COL_4, latitude);
        contentValues.put(COL_5, dt);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }


}
