package com.example.onlineparking.Login;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PersonResponse implements Parcelable {
    private String _id;
    private String username, floor, slot, car, timein,timeout, date;
    private List<String> slots;

    protected PersonResponse(Parcel in) {
        _id = in.readString();
        username = in.readString();
        floor = in.readString();
        slot = in.readString();
        car = in.readString();
        timein = in.readString();
        timeout = in.readString();
        date = in.readString();
        slots = in.createStringArrayList(); // Read a list of strings
    }

    public static final Parcelable.Creator<PersonResponse> CREATOR = new Parcelable.Creator<PersonResponse>() {
        @Override
        public PersonResponse createFromParcel(Parcel in) {
            return new PersonResponse(in);
        }

        @Override
        public PersonResponse[] newArray(int size) {
            return new PersonResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(username);
        dest.writeString(floor);
        dest.writeString(slot);
        dest.writeString(car);
        dest.writeString(timein);
        dest.writeString(timeout);
        dest.writeString(date);
        dest.writeStringList(slots); // Write a list of strings
    }

    public String get_id() {
        return _id;
    }

    public String getUsername() {
        return username;
    }

    public String getFloor() {
        return floor;
    }

    public String getSlot() {
        return slot;
    }

    public String getCar() {
        return car;
    }

    public String getTimein() {
        return timein;
    }

    public String getTimeout() {
        return timeout;
    }

    public String getDate() {
        return date;
    }

    public List<String> getSlots() {
        return slots;
    }
}
