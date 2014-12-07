package com.example.gt.smartsleeper;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends Activity {

    //tag for logcat output (e.g. console output)
    private static final String TAG = MainActivity.class.getSimpleName();
    // Declaring and initializing the extra message to cycles value
    public static String EXTRA_CYCLES = "com.example.gt.smartsleeper.CYCLES";
    public int hour;
    public int minute;

    public static int bhour;
    public static int bminute;
    public static String btimepickertime;
    public static int whour;
    public static int wminute;
    public static String wtimepickertime;

    static final int TIME_DIALOG_ID=1;

    public static TextView bdTime;
    public static TextView wakeTime;
    private TimePicker timePicker1;
    public static String alarmTime;
    public static int fallTime = 14; //minutes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //screen content set to activity_main.xml
        setContentView(R.layout.activity_main);
/*        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        //menu content set to main.xml
        return super.onCreateOptionsMenu(menu);

//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
    }

    @Override
    // Handle presses on the action bar items
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("alarmTime", alarmTime);
        savedInstanceState.putInt("fallTime", fallTime);
        savedInstanceState.putString("bedTime", btimepickertime);
        savedInstanceState.putString("wakeTime", wtimepickertime);
        savedInstanceState.putInt("bhour", bhour);
        savedInstanceState.putInt("bminute", bminute);
        savedInstanceState.putInt("whour", whour);
        savedInstanceState.putInt("wminute", wminute);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        alarmTime = savedInstanceState.getString("alarmTime");
        fallTime = savedInstanceState.getInt("fallTime");
        btimepickertime = savedInstanceState.getString("bedTime");
        wtimepickertime = savedInstanceState.getString("wakeTime");
        bhour = savedInstanceState.getInt("bhour");
        bminute = savedInstanceState.getInt("bminute");
        whour = savedInstanceState.getInt("whour");
        wminute = savedInstanceState.getInt("wminute");
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
            View rootView = inflater.inflate(R.layout.activity_main, container, false);
            return rootView;
        }
    }

    public void setPreferences(View view) {
        // Set Preferences: avg bed time, # of sleep cycles before wake up in response to button click
        Log.d(TAG,"Set Alarm button pressed");

        Date alarm = calculateAlarm(bhour,bminute,whour,wminute);
        Log.d(TAG,"Alarm set to: "+alarm);

        TextView tv = (TextView) this.findViewById(R.id.textView_alarmTime);
        tv.setText("" + String.format("%s\n %tl:%<tM%<tp", "Alarm set for", alarm));


 /*       Intent intent = new Intent(this, ConfirmationActivity.class);
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
        startActivity(intent);*/
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
        Log.d(TAG,"show timepickerdialog function called");
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");

        //creating a new bundle that includes dialogType
        Bundle args = new Bundle();
        args.putInt("dialogType",0);
        newFragment.setArguments(args);
        Log.d(TAG,"end of showTimePickerDialog");
    }
    public void showBedTimePickerDialog(View v) {
        Log.d(TAG,"show timepickerdialog function called");
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");

        //creating a new bundle that includes dialogType
        Bundle args = new Bundle();
        args.putInt("dialogType",1);
        newFragment.setArguments(args);
        Log.d(TAG,"end of showTimePickerDialog");
    }
    public void showWakeTimePickerDialog(View v) {
        Log.d(TAG,"show timepickerdialog function called");
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");

        //creating a new bundle that includes dialogType
        Bundle args = new Bundle();
        args.putInt("dialogType",2);
        newFragment.setArguments(args);
        Log.d(TAG,"end of showTimePickerDialog");
    }

    //not in use right now!
    // Create  TimePickerDialog listener
/*    private TimePickerDialog.OnTimeSetListener bTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                // the callback received when the user "sets" the TimePickerDialog in the dialog
                public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                    hour = hourOfDay;
                    minute = min;

                    Log.d(TAG, "timepicker in main: " + hour + ":" + minute);

                    bdTime.setText(hour+":"+minute);

*//*                    //set textview accordingly
                    TextView tv = (TextView) this.findViewById(R.id.textView_bedTime);
                    tv.setText(""+ btimepickertime);*//*
                }


            };*/

    /*TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(android.widget.TimePicker view,
                                      int hourOfDay, int minute) {
                    Log.i("",""+hourOfDay+":"+minute);
                }
            };*/

/*    // display current time
    public void setCurrentTimeOnView() {

        bdTime = (TextView) findViewById(R.id.textView_bedTime);
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

    }*/

    //not in use right now!
/*    // Method automatically gets Called when you call showDialog()  method
    @Override
    protected Dialog onCreateDialog(int id) {

        Log.d(TAG,"onCreateDialog() called");

        switch (id) {

            // create a new TimePickerDialog with values you want to show
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        bTimeSetListener, bhour, bminute, false);

        }
        return null;
    }*/

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public Date calculateAlarm (int bhour, int bmin, int whour, int wmin) {
        // Calculates what time to set the alarm to.
        // Alarm should be set to rounddown of
        // = (wake time - bed time)/90 minutes + time req'd to fall asleep
        Date alarm = new Date();
        System.out.println("date: "+ String.format("%tc", alarm));
        int cycles = 0; // maximum number of sleep cycles
        long diff = 0;
        int alarmTime = 0;

        // Convert bed time and wake time hours and minutes to Date format
        try {
            java.text.DateFormat df = new java.text.SimpleDateFormat("hh:mm");
            System.out.println("converting bedtime to date");
            java.util.Date bedTime = df.parse("" + bhour + ":" + bmin);
            java.util.Date wakeTime = df.parse("" + whour + ":" + wmin);

            // Finding the time difference between wake time and bed time
            System.out.println("subtraction: " + wakeTime.getTime() + " - " + bedTime.getTime());
            if (wakeTime.getTime() >= bedTime.getTime()) {
                // wake up time is on the same day as bed time
                // (e.g. nap on Sat aft, wake Sat night)
                diff = wakeTime.getTime() - bedTime.getTime();
            }
            else {
                // wake up time is on the next day
                // (e.g. sleep on Sat night, wake on Sun morning)
                System.out.println("subtraction: " + wakeTime.getTime() + " + (86400000 - "+ bedTime.getTime() +")");
                diff = wakeTime.getTime() + (86400000 - bedTime.getTime());
            }
            System.out.println("diff:" + diff);
            
            // Calculating maximum # of sleep cycles possible between bed time and wake time.
            if(diff<=fallTime*60000){
                cycles = 0; // when cycles would be otherwise negative
            } else {
                cycles = (int) Math.floor((diff - fallTime * 60000) / (3600000 * 1.5));
            }
            System.out.println("cycles:" + cycles);
            // Calculating the time the alarm should ring
            System.out.println("alarm time calculation: " + bedTime.getTime() +","+ cycles +","+ fallTime);
            if(cycles == 0) {
                // Assumption: Users will be taking a short nap if cycles=0
                // and are not concerned with sleep cycles.
                alarmTime = (int) wakeTime.getTime();
            } else {
                alarmTime = (int) Math.floor(bedTime.getTime() + cycles * 1.5 * 3600000 + (fallTime * 60000));
            }
            System.out.println("alarm time: "+alarmTime);

//            // Error handling in case alarm time is outside of bed to wake time range.
//            if (alarmTime > wakeTime.getTime() || alarmTime < bedTime.getTime()){
//                alarmTime = (int) wakeTime.getTime();
//            }

            // Converting alarm time to Date format
            alarm = new Date(alarmTime);
            System.out.printf("%s %tl:%<tM%<tp\n", "Date:", alarm);
            System.out.println(""+ String.format("%tc", alarm));
            
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return alarm;
    }
}
