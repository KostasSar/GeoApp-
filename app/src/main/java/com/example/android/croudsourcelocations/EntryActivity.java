package com.example.android.croudsourcelocations;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EntryActivity extends AppCompatActivity {
    LocationsHelper myDb;
    EditText ID, Longitude, Latitude, Time, Date, Delete;
    Button buttSubmit, buttDelete;

    public Button buttSearchOtherEntries;

    public void goToSearch(){
        buttSearchOtherEntries = (Button)findViewById(R.id.buttSearchOtherEntries);
        buttSearchOtherEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent showSearchActivity = new Intent(EntryActivity.this, SearchEntries.class);
                startActivity(showSearchActivity);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new LocationsHelper(this);

        /* goToSearch is called when the user presses the "Search other activities" button.
           Shows the "activity_entry" where the user can perform a search in the database. */
        setContentView(R.layout.activity_entry);
        goToSearch();


        //Casting variables where we are getting the information to be inserted in DB
        ID = (EditText)findViewById(R.id.editID);
        Longitude = (EditText)findViewById(R.id.editLongitude);
        Latitude =  (EditText)findViewById(R.id.editLatitude);
        Time =  (EditText)findViewById(R.id.editTime);
        Date =  (EditText)findViewById(R.id.editDate);
        buttSubmit = (Button)findViewById(R.id.buttSubmit);

        /*This section adds the current date and time to the according "EditText"s
            If the user wants to insert other information they remain editable.   */
        SimpleDateFormat dateForm = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
        SimpleDateFormat timeForm = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String date = dateForm.format(Calendar.getInstance().getTime());
        String time = timeForm.format(Calendar.getInstance().getTime());

        Date.setText(date);
        Time.setText(time);

        //SubmitInfo is called whenever the user presses the "Submit" button, after he has filled the needed info.
        submitInfo();

        //casting the button and EditText for the delete method
        Delete = (EditText)findViewById(R.id.editDelete);
        buttDelete = (Button) findViewById(R.id.buttDelete);

        delete();


    }

    public void delete(){
        myDb = new LocationsHelper(this);

        buttDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db = myDb.getWritableDatabase();

                //check if "delete id" field is empty
                if(TextUtils.isEmpty(Delete.getText().toString().trim())){
                    Delete.setError("Field can not be empty");
                }

                String[] args= {Delete.getText().toString().trim()};
                boolean result = db.delete(myDb.TABLE_NAME, myDb.COL_1 + " =?" , args) > 0;

                if(result == true){
                    Toast.makeText(EntryActivity.this, "Row Deleted", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(EntryActivity.this, "Row NOT Deleted", Toast.LENGTH_LONG).show();

                }
            }
        });

    }


    /*This method adds a listener to the submit button and calls the insertLocation method to insert the
      inserted information to the database.  */
    public void submitInfo(){
        buttSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Check if all EditText fields were filled by the user.
                 If yes, proceed. If not error appears on empty field.

                 PLUS check if each field is compatible with the SQL querry. */

                //UserID field should not be empty or be longer than 10chars.
                if (TextUtils.isEmpty(ID.getText().toString().trim())) {
                    ID.setError("Field can not be empty.");
                    return;
                } else if (ID.getText().toString().trim().length() > 10) {
                    ID.setError("Lenght up to 10 characters");
                }

                //Longitude and Latitude fields should not be empty, and should be filled with decimal.
                if(TextUtils.isEmpty(Longitude.getText().toString().trim())) {
                    Longitude.setError("Field can not be empty.");
                    return;
                }
                try{
                    float testLong = Float.parseFloat(Longitude.getText().toString().trim());
                }catch(NumberFormatException e){
                    Longitude.setError("Input should be a number.");
                }

                if(TextUtils.isEmpty(Latitude.getText().toString().trim())) {
                    Latitude.setError("Field can not be empty.");
                    return;
                }
                try{
                    float testLat = Float.parseFloat(Latitude.getText().toString().trim());
                }catch(NumberFormatException e){
                    Latitude.setError("Input should be a number.");
                }

                //Time field should have a hh.mm format and should not be empty.
                if(TextUtils.isEmpty(Time.getText().toString().trim())) {
                    Time.setError("Field can not be empty.");
                    return;
                }else if(Time.getText().toString().trim().length() > 5){
                    Time.setError("Input too long");
                }

                //Date field should not be empty and has dd mmm yyyy format (ex. 12 Dec 2017)
                if(TextUtils.isEmpty(Date.getText().toString().trim())) {
                    Date.setError("Field can not be empty.");
                    return;
                }else if(Date.getText().toString().trim().length() > 11){
                    Date.setError("Input too long.");
                }




                //Concatenating the date and time Strins to create a timestamp (dt = timestamp).
//                String dt = Date.getText().toString().concat(" ");
//                dt.concat(Time.getText().toString());
                String dt = Date.getText().toString() + " " + Time.getText().toString();

                //Inserting info into db using insertLocation() from LocationsHelper class.
                boolean result = myDb.insertLocation(ID.getText().toString(),
                                    Float.valueOf(Longitude.getText().toString() ),
                                    Float.valueOf(Latitude.getText().toString() ),
                                    dt);

                /*Depending on the "result" variable value the according message is shown to the user.
                    "Data Inserted" when true,
                    "Data NOT Inserted" when false. */
                if(result == true){
                    Toast.makeText(EntryActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(EntryActivity.this, "Data NOT Inserted", Toast.LENGTH_LONG).show();

                }
            }
        });

    }


}
