package com.example.gt.smartsleeper;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.net.ParseException;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = TimePickerFragment.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Pulling out dialogType from the Bundle.
        int dialogType = getArguments().getInt("dialogType");
        Log.d(TAG,""+dialogType);

        boolean currentTimeFlag = false;
        // Initializing variables containing default timepicker values
        int hour = 0;
        int minute = 0;

        // Use bed/wake time if it was already set by user.
        if (dialogType==1){
            if (!MainActivity.btimepickertime.equals("0")) {
                hour = MainActivity.bhour;
                minute = MainActivity.bminute;
            } else { currentTimeFlag = true; }
        } else if (dialogType==2) {
            if (!MainActivity.wtimepickertime.equals("0")) {
                hour = MainActivity.whour;
                minute = MainActivity.wminute;
            } else { currentTimeFlag = true; }
        } else {
            currentTimeFlag = true;
        }

        // Use the current time as the default values for the picker
        if (currentTimeFlag==true) {
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }

        Log.d(TAG,"dialog creation");
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Set a temporary variable with the user's selected time

        //convert time components to dateformat
        String time = ""+hourOfDay+":"+minute;
        Log.d(TAG,""+time);

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(time);
            System.out.println(dateObj);
            time = new SimpleDateFormat("h:mma").format(dateObj).toLowerCase(); //"3:53am", remove toLowerCase() for uppercase AM/PM
        } catch (final ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Log.d(TAG,""+time);

        // pulling out dialogType from the bundle
        int dialogType=getArguments().getInt("dialogType");
        Log.d("TimePicker onTimeSet",""+dialogType);

        if (dialogType == 1) {

            MainActivity.bhour = hourOfDay;
            MainActivity.bminute = minute;

            MainActivity.btimepickertime = time;
            Log.d(TAG, "bed time in timepicker in frag: " + MainActivity.btimepickertime);

            TextView tv = (TextView) getActivity().findViewById(R.id.textView_bedTime);
            tv.setText("" + MainActivity.btimepickertime);
        }
        else if (dialogType == 2) {
            MainActivity.whour = hourOfDay;
            MainActivity.wminute = minute;

            MainActivity.wtimepickertime = time;
            Log.d(TAG, "wake time in timepickerfrag: " + MainActivity.wtimepickertime);

            TextView tv = (TextView) getActivity().findViewById(R.id.textView_wakeTime);
            tv.setText("" + MainActivity.wtimepickertime);
        }
        else {
            Log.d(TAG, "Timepicker called with dialogType = 0!");
        }

    }

}