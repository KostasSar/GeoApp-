package com.example.android.croudsourcelocations;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchEntries extends AppCompatActivity {

    Button buttSearch;
    LocationsHelper locHelp;
    Spinner id_spinner, dt_spinner;

    public void goToResults(){
        buttSearch = (Button)findViewById(R.id.buttSearch);
        buttSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Gets the user to the "Display Entries" activity where the search result is shown.
                Intent showResultsActivity = new Intent(SearchEntries.this, DisplayEntries.class);
                showResultsActivity.putExtra("selectedID", id_spinner.getSelectedItem().toString());
                showResultsActivity.putExtra("selectedDT", dt_spinner.getSelectedItem().toString());
                startActivity(showResultsActivity);
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_entries);
        goToResults();
        locHelp = new LocationsHelper(this);
        SQLiteDatabase db = locHelp.getReadableDatabase();

        /*Casting 2 spinners. First will display userids, second will display timestamps.
        The second one will remain inactive until an item is selected on the first one.   */
        id_spinner = (Spinner)findViewById(R.id.id_spinner);
        dt_spinner = (Spinner)findViewById(R.id.dt_spinner);

        //Reading UserIDs and TimeStamps from Db to put into spinners.
        String[] columns = {LocationsHelper.COL_2, LocationsHelper.COL_5};
        Cursor res = db.query(LocationsHelper.TABLE_NAME, columns , LocationsHelper.COL_1, null, null, null, null);

        ArrayList<String> idResults = new ArrayList<String>();
        ArrayList<String> dtResults = new ArrayList<String>();

        while(res.moveToNext()){
            idResults.add(res.getString(0));
            dtResults.add(res.getString(1));
        }

        //Creating adapters so that spinners can get data from the arraylists (idResults and dtResults)
        ArrayAdapter arrAd = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, idResults);
        arrAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        id_spinner.setAdapter(arrAd);

        ArrayAdapter arrAd1 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dtResults);
        arrAd1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dt_spinner.setAdapter(arrAd1);
    }
}
