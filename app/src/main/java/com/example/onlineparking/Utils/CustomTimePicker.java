package com.example.onlineparking.Utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

public class CustomTimePicker {

    private Context context;
    private OnTimeSelectedListener onTimeSelectedListener;

    public CustomTimePicker(Context context) {
        this.context = context;
    }

    public void showTimePickerDialog() {
        // Get the current time
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = calendar.get(java.util.Calendar.MINUTE);

        // Create a time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                (view, hourOfDay, minute1) -> {
                    if (onTimeSelectedListener != null) {
                        onTimeSelectedListener.onTimeSelected(convertTo12Hour(hourOfDay), minute1, getAMPM(hourOfDay));
                    }
                },
                hour,
                minute,
                false
        );

        timePickerDialog.show();
    }

    private int convertTo12Hour(int hourOfDay) {
        // Convert to 12-hour format
        int hour = hourOfDay % 12;
        if (hour == 0) {
            hour = 12;
        }
        return hour;
    }

    private String getAMPM(int hourOfDay) {

        return (hourOfDay < 12) ? "AM" : "PM";
    }

    public void setOnTimeSelectedListener(OnTimeSelectedListener listener) {
        this.onTimeSelectedListener = listener;
    }

    public interface OnTimeSelectedListener {
        void onTimeSelected(int hourOfDay, int minute, String shift);
    }
}

