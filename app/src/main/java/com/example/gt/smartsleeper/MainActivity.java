package com.example.gt.smartsleeper;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.view.MenuInflater;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


public class MainActivity extends Activity {

    // Declaring and initializing the extra message to cycles value
    public static String EXTRA_CYCLES = "com.example.gt.smartsleeper.CYCLES";
    public int hour;
    public int minute;

    public static int bhour;
    public static int bminute;

    static final int TIME_DIALOG_ID=1;

    public static TextView  bdTime;
    private TimePicker timePicker1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            /*case R.id.action_search:
                // Action to perform to Search
                //openSearch();
                return true; //temp, until function written*/
            case R.id.action_settings:
                // Action to perform to open Settings
                //openSettings();
                return true; //temp, until function written
            default:
                return super.onOptionsItemSelected(item);
        }

//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public void setPreferences(View view) {
        // Set Preferences: avg bed time, # of sleep cycles before wake up in response to button click
        Intent intent = new Intent(this, ConfirmationActivity.class);
        NumberPicker numberPicker  = (NumberPicker) findViewById(R.id.cycles);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(0);
        int sleepCycles = numberPicker.getValue();
//        String sleepCycles = editText.getText().toString();
        intent.putExtra(EXTRA_CYCLES, sleepCycles);

        // Set preference variables in DB
        //throw error check and catch
//        try {
//        int sleepCycles = Integer.parseInt(sleepCycles);
//        } catch (NumberFormatException nfe){
//            //show error message in Main Fragment
//            System.out.println("Received a non-integer input.");
//            //do not record any values
//        }

        // Start next activity (e.g. display confirmation message)
        startActivity(intent);
    }

    //should merge this with setSleepCycles as setPreferences
//    public void setBedTime(View view) {
//        // Set average start time of a night's sleep
//        Intent intent = new Intent(this, ConfirmationActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
//    }

    public void openSettings(){
        // call Settings Activity

        // Field for average bed time

        // Field for # of sleep cycles preferred

    }
    public void openSearch(){
        //unused
    }

    public void showNumberPickerDialog(View v) {
        DialogFragment newFragment = new NumberPickerFragment();
        newFragment.show(getFragmentManager(), "numberPicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    // display current time
    public void setCurrentTimeOnView() {

        bdTime = (TextView) findViewById(R.id.bdTime);
        //timePicker1 = (TimePicker) findViewById(R.id.bedTime);

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        // set current time into textview
        bdTime.setText(
                new StringBuilder().append(pad(hour))
                        .append(":").append(pad(minute)));

        // set current time into timepicker
        timePicker1.setCurrentHour(hour);
        timePicker1.setCurrentMinute(minute);

    }

    // Register  TimePickerDialog listener
    private TimePickerDialog.OnTimeSetListener bTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                // the callback received when the user "sets" the TimePickerDialog in the dialog
                public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                    hour = hourOfDay;
                    minute = min;

                    bdTime.setText(hour+":"+minute);

                }
            };


    // Method automatically gets Called when you call showDialog()  method
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {

            // create a new TimePickerDialog with values you want to show
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        bTimeSetListener, bhour, bminute, false);

        }
        return null;
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}
