package com.mangoman.sms.main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Harshit Rastogi on 9/21/2014 at 11:56 AM.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private final String LOGTAG = ((Object) this).getClass().getSimpleName();
    private static DateSelectedListener listener;
    private int beginOrEndDate = 1;
    private String initialDate;

    public interface DateSelectedListener {
        public void onDateSelected(long milliseconds, int beginOrEndDate);
    }

    public DatePickerFragment() {
    }

    public void setListener(DateSelectedListener listener) {
        DatePickerFragment.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        this.beginOrEndDate = getArguments().getInt("type");
        this.initialDate = getArguments().getString("initialdate");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int year = 2014, month = 4, day = 10;
        try {
            Date inD = sdf.parse(initialDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(inD);
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Log.d(LOGTAG, "Year : " + year + " month : " + month + " day " + day);

        String dateString = (month + 1) + " " + day + " " + " " + year;
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
        long milliseconds = 0;
        try {
            Date date = sdf.parse(dateString);
            milliseconds = date.getTime();
        } catch (Exception e) {
            Log.w(LOGTAG, "Couldn't parse date : " + dateString);
        }

        if (listener != null) {
            listener.onDateSelected(milliseconds, beginOrEndDate);
        }

    }
}