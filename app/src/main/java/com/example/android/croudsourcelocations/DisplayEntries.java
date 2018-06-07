package com.example.android.croudsourcelocations;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.android.croudsourcelocations.LocationsHelper.COL_5;

public class DisplayEntries extends AppCompatActivity {

    LocationsHelper locHelp;
    TextView displayId, displayUserId, displayLongitude, displayLatitude, displayDt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_entries);

        //Retrieving the user's selected items from the spinners on the "SearchEntries" activity.
        String selectedID = getIntent().getStringExtra("selectedID");
        String selectedDT = getIntent().getStringExtra("selectedDT");

        //Casting database to perform search and textviews where the entry information will be displayed.
        locHelp = new LocationsHelper(this);
        SQLiteDatabase db = locHelp.getReadableDatabase();

        displayId = (TextView)findViewById(R.id.displayId);
        displayUserId = (TextView) findViewById(R.id.displayUserId);
        displayLongitude = (TextView)findViewById(R.id.displayLongitude);
        displayLatitude = (TextView)findViewById(R.id.displayLatitude);
        displayDt = (TextView)findViewById(R.id.displayDt);

        //Perform search in database checking if there is an entry matching the selected items.
        String selection = LocationsHelper.COL_2 + "=? AND " + LocationsHelper.COL_5 + "=?";
        String[] selectionArgs = {selectedID, selectedDT};
        Cursor res = db.query(LocationsHelper.TABLE_NAME, null , selection, selectionArgs, null, null, null);

        //If there is no result display message.
        if(res.getCount() == 0){
            Toast.makeText(DisplayEntries.this, "No result. Select other ID and/or TimeStamp", Toast.LENGTH_LONG).show();
            return;
        }

        //If there is a result display on TextViews.
        while(res.moveToNext()) {
            displayId.setText("ID: " + String.valueOf(res.getInt(0)));
            displayUserId.setText("User ID: " + res.getString(1));
            displayLongitude.setText("Longitude: " + String.valueOf(res.getFloat(2)));
            displayLatitude.setText("Latitude: " + String.valueOf(res.getFloat(3)));
            displayDt.setText("TimeStamp: " + res.getString(4));
        }
    }
}
