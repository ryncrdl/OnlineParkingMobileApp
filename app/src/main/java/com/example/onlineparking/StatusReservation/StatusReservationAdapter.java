package com.example.onlineparking.StatusReservation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineparking.R;
import com.example.onlineparking.Login.PersonResponse;

import java.util.List;

public class StatusReservationAdapter extends RecyclerView.Adapter<StatusReservationViewHolder> {
    private List<PersonResponse> persons;
    private Context context;

    public StatusReservationAdapter(Context context, List<PersonResponse> persons) {
        this.persons = persons;
        this.context = context;
    }

    @NonNull
    @Override
    public StatusReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_reservation_status_list, parent, false);
        return new StatusReservationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusReservationViewHolder holder, int position) {
        PersonResponse person = persons.get(position);
        holder.carName.setText(person.getCar());
        if(person.getTimein().contains("none")){
            holder.timein.setText("--");
        }else{
            holder.timein.setText(person.getTimein());
        }

        if(person.getTimeout().contains("none") && person.getTimein().contains("none")){
            holder.timeout.setText("--");
        }else{
            holder.timeout.setText(person.getTimeout());
        }

        if(person.getDate().contains("none") && person.getTimeout().contains("none") && person.getTimein().contains("none")){
            holder.date.setText("--");

        }else{
            holder.date.setText(person.getDate());
        }

        if(person.getFloor().contains("Select Floor")){
            holder.floor.setText("--");
        }else{
            holder.floor.setText(person.getFloor());
        }

        if(person.getSlot().contains("Select Slot")){
            holder.slot.setText("--");
        }else{

            holder.slot.setText(person.getSlot());
        }
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }
}
