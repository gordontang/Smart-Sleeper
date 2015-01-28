package ca.gordontang.smartsleeper;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.view.MenuInflater;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends Activity {

    //receiver for screen on/off state
    private BroadcastReceiver mReceiver = null;
    //tag for logcat output (e.g. console output)
    private static final String TAG = MainActivity.class.getSimpleName();

    // Declaring and initializing the extra message to cycles value
    public static String EXTRA_CYCLES = "com.example.gt.smartsleeper.CYCLES";

    public static int bhour=0;
    public static int bminute=0;
    public static String btimepickertime="0";
    public static int whour=0;
    public static int wminute=0;
    public static String wtimepickertime="0";

    public static TextView bdTime;
    public static TextView wkTime;
    public static String alarmTime;
    public static int fallTime = 14; //in minutes

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

        // initialize screen state receiver
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
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
    protected void onPause() {
        // when the screen is about to turn off or app is moving to background
        super.onPause();
        //onPause() is executed prior to ScreenReceiver's update
        //wasScreenOn is irrelevant here. isScreenOn is the state before onPause.
        //Cannot check what the new screen state is (either On/Off).
        //isScreenOn will always be TRUE, so it does not provide useful info
        Log.d(TAG, "onPause");
    }

    public int numHandlers = 0;
    public Handler handler = new Handler();
    @Override
    protected void onStop() {
        super.onStop();
        // when the screen is about to turn off or app will be moved to background
        if (ScreenReceiver.isScreenOn) {
            // this is when onStop() is called due to a screen state change
            Log.e(TAG, "onStop: screen was on");
        } else {
            // this is when onStop() is called when the screen state has not changed (still on)
            Log.e(TAG, "onStop: screen was off");
        }

        if (ScreenReceiver.isScreenOn) {
            // saving time of onStop() call
            final Calendar c = Calendar.getInstance();
            final int bh = c.get(Calendar.HOUR_OF_DAY);
            final int bm = c.get(Calendar.MINUTE);
            //defining the Runnable which Handler will call
            Runnable r = new Runnable() {
                @Override
                public void run() { // Set the alarm clock
                    Log.d(TAG, "handler's delayed run called");
                    // Do after time delay if screen has been turned off
                    if (!ScreenReceiver.isScreenOn) {
                        Log.d(TAG, "Set alarm called from onStop()");
                        //Set global bed time variables to time of last onStop()
                        bhour = bh;
                        bminute = bm;
                        //Set alarm
                        setAlarm();
                        numHandlers = 0;
                        Log.e(TAG, "After time delay, alarm is set and numHandlers = "+numHandlers);
                    } else {
                        //do nothing
                        Log.d(TAG,"handler: Screen was on, so I did nothing");
                        numHandlers = 0;
                    }
                    Log.d(TAG,"numHandlers: "+numHandlers);
                }
            };
            Log.d(TAG,"numHandlers (before check): "+numHandlers);
            if (numHandlers>=1){
                //remove all callbacks from queue
                handler.removeCallbacksAndMessages(null);
                numHandlers=0;
                Log.d(TAG,"old handlers removed, numHandlers="+numHandlers);
                //only the newest Runnable callback will be executed
            }
            Log.d(TAG,"new onStop handler added to task queue. H:"+bh+"M:"+bm);
                numHandlers=1;
                Log.d(TAG,"numHandlers set to: "+numHandlers);
            //Time delay: 60 minutes = 3600000ms
            final int timeDelay = 3600000;
            //Post the Runnable callback to the queue
            handler.postDelayed(r, timeDelay);

        } else {
            Log.d(TAG,"no new handler as screen was off");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // only when screen turns on or app is opened
        if (ScreenReceiver.wasScreenOn!=ScreenReceiver.isScreenOn && !ScreenReceiver.isScreenOn) {
            // this is when onResume() is called due to a screen state change
            Log.e(TAG, "onResume: screen was off");
        } else {
            // this is when onResume() is called when the screen state has not changed
            Log.e(TAG, "onResume: screen was on");
        }
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onDestroy();
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

    public void setPreferences(View view){
        setAlarm();
    }

    public void setAlarm() {
        // Set Preferences: avg bed time, # of sleep cycles before wake up in response to button click
        Log.d(TAG,"Set Alarm button pressed");

        //calculate what time the alarm should go off
        Date alarm = calculateAlarmTime(bhour, bminute, whour, wminute);
        Log.d(TAG,"Alarm time calculated: "+alarm);

        setCalculatedTimeText(alarm);
        Log.d(TAG,"Calculated time updated");

//trying to create a separate class for the alarm clock functionality
        //boolean alarmSuccess = ManageAlarmClock.setAlarm(alarm);

        // Getting time values (hour and minute) from Date variable
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(alarm);   // assigns calendar to given date
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        int minute = calendar.get(Calendar.MINUTE); // gets minute

        //sets default Alarm Clock app with time
        setDefaultAlarm(hour, minute);
        // Update on screen text to alarm time
        TextView tv = (TextView) this.findViewById(R.id.textView_alarmTime);
        tv.setText("" + String.format("%s\n %tl:%<tM%<tp", "Alarm set for", alarm));
    }

    public void calculateAlarm() {
        Date alarm = calculateAlarmTime(bhour, bminute, whour, wminute);
        Log.d(TAG,"Alarm time calculated: "+alarm);
        setCalculatedTimeText(alarm);
        Log.d(TAG,"Calculated time updated");
    }

    public void setCalculatedTimeText(Date alarm) {
        // Update on screen calculated time to alarm time
        TextView tv = (TextView) this.findViewById(R.id.textView_calcTime);
        tv.setText("" + String.format("%tl:%<tM%<tp", alarm));
    }

    public void setDefaultAlarm(int hour, int minute){
        // Setting an alarm on the Alarm Clock app with time
        // Note: can only set times in next 24 hours
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_MESSAGE, "Smart Alarm");
        i.putExtra(AlarmClock.EXTRA_HOUR, hour);
        i.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        //i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        startActivity(i);
        Log.d(TAG, "AlarmClock set for (h:m): " + hour + ":" + minute);
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
        args.putInt("dialogType", 1);
        newFragment.setArguments(args);
        Log.d(TAG, "end of showTimePickerDialog");
    }
    public void showWakeTimePickerDialog(View v) {
        Log.d(TAG,"show timepickerdialog function called");
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");

        //creating a new bundle that includes dialogType
        Bundle args = new Bundle();
        args.putInt("dialogType", 2);
        newFragment.setArguments(args);
        Log.d(TAG, "end of showTimePickerDialog");
    }

    public static Date calculateAlarmTime(int bhour, int bmin, int whour, int wmin) {
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
