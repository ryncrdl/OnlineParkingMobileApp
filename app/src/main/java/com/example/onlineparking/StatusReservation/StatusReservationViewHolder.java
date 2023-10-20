package com.example.onlineparking.StatusReservation;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.onlineparking.R;

public class StatusReservationViewHolder extends RecyclerView.ViewHolder
{
    TextView carName, timein, timeout, date,floor,slot;
    public StatusReservationViewHolder(@NonNull View reservationStatus){
        super(reservationStatus);
        carName = reservationStatus.findViewById(R.id.car_name);
        timein = reservationStatus.findViewById(R.id.timein);
        timeout = reservationStatus.findViewById(R.id.timeout);
        date = reservationStatus.findViewById(R.id.date);
        floor = reservationStatus.findViewById(R.id.floor);
        slot = reservationStatus.findViewById(R.id.slot);
    }
}
