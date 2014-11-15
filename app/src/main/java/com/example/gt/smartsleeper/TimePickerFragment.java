package com.example.gt.smartsleeper;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = TimePickerFragment.class.getSimpleName();

    private TextView t;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        Log.d(TAG,"dialog created");

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

    }
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        // Set a temporary variable with the user's average bed time
        MainActivity.bhour = hourOfDay;
        MainActivity.bminute = minute;

        MainActivity.btimepickertime = ""+MainActivity.bhour+":"+MainActivity.bminute;
        Log.d(TAG, "timepicker in frag: " + MainActivity.btimepickertime);

        TextView tv = (TextView) getActivity().findViewById(R.id.textView_bedTime);
        tv.setText(""+ MainActivity.btimepickertime);
    }

}