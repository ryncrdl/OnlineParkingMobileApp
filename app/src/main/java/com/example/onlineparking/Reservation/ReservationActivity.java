package com.example.onlineparking.Reservation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineparking.Api.ApiClient;
import com.example.onlineparking.Api.ApiEndpoints;
import com.example.onlineparking.Dashboard.DashboardActivity;
import com.example.onlineparking.Login.PersonResponse;
import com.example.onlineparking.R;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReservationActivity extends AppCompatActivity {

    TextView f1, f2, f3, f4, l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12;
    LinearLayout lotsContainer;
    AppCompatButton btnDashboard;

    private ProgressBar progressBar;

    private String personId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        progressBar = findViewById(R.id.progress_bar);
        btnDashboard = findViewById(R.id.backHome);

        btnDashboard.setOnClickListener(view -> {
            startActivity(new Intent(ReservationActivity.this, DashboardActivity.class));
        });

        lotsContainer = findViewById(R.id.lots_container);

        f1 = findViewById(R.id.floor1);
        f2 = findViewById(R.id.floor2);
        f3 = findViewById(R.id.floor3);
        f4 = findViewById(R.id.floor4);

        l1 = findViewById(R.id.lot1);
        l2 = findViewById(R.id.lot2);
        l3 = findViewById(R.id.lot3);
        l4 = findViewById(R.id.lot4);
        l5 = findViewById(R.id.lot5);
        l6 = findViewById(R.id.lot6);
        l7 = findViewById(R.id.lot7);
        l8 = findViewById(R.id.lot8);
        l9 = findViewById(R.id.lot9);
        l10 = findViewById(R.id.lot10);
        l11 = findViewById(R.id.lot11);
        l12 = findViewById(R.id.lot12);

        f1.setOnClickListener(view -> {
            ResetBackgroundColor();
            displayAvailableLots();
            f1.setBackgroundResource(R.drawable.bg1);
            fetchAndDisplaySlots("1");
        });
        f2.setOnClickListener(view -> {
            ResetBackgroundColor();
            displayAvailableLots();
            f2.setBackgroundResource(R.drawable.bg1);
            fetchAndDisplaySlots("2");
        });
        f3.setOnClickListener(view -> {
            ResetBackgroundColor();
            displayAvailableLots();
            f3.setBackgroundResource(R.drawable.bg1);
            fetchAndDisplaySlots("3");
        });
        f4.setOnClickListener(view -> {
            ResetBackgroundColor();
            displayAvailableLots();
            f4.setBackgroundResource(R.drawable.bg1);
            fetchAndDisplaySlots("4");
        });
    }

    private void displayAvailableLots() {
        lotsContainer.setVisibility(View.VISIBLE);
    }

    private void ResetBackgroundColor() {
        f1.setBackgroundResource(R.drawable.border);
        f2.setBackgroundResource(R.drawable.border);
        f3.setBackgroundResource(R.drawable.border);
        f4.setBackgroundResource(R.drawable.border);
    }

    private void changeSlotBackground(TextView textView, boolean isOccupied) {
        if (isOccupied) {
            textView.setBackgroundResource(R.drawable.bg1);
        } else {
            textView.setBackgroundResource(R.drawable.border);
        }
    }

    public void fetchAndDisplaySlots(String floorNumber) {
        showLoading();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("floor", floorNumber);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        ApiClient apiClient = new ApiClient();
        ApiEndpoints apiService = apiClient.getApiService();
        Call<PersonResponse> call = apiService.checkSlots(requestBody);


        call.enqueue(new Callback<PersonResponse>() {
            @Override
            public void onResponse(Call<PersonResponse> call, Response<PersonResponse> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    PersonResponse fetchedSlots = response.body();

                    if (fetchedSlots != null) {
                        List<String> slots = fetchedSlots.getSlots();

                        TextView[] allTextViews = {l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12};

                        // Iterate through all the TextViews
                        for (int i = 0; i < allTextViews.length; i++) {
                            TextView textView = allTextViews[i];
                            String slotNumber = String.valueOf(i + 1);
                            int finalI = i;

                            if (slots.contains(slotNumber)) {
                                changeSlotBackground(textView, true);
                            } else {
                                // Slot is available, set a different background (e.g., border)
                                changeSlotBackground(textView, false);
                            }

                        }

                        for (int i = 0; i < allTextViews.length; i++) {
                            TextView textView = allTextViews[i];
                            int finalI = i;

                            // Add an OnClickListener to each TextView
                            textView.setOnClickListener(view -> {
                                String slotNumber = String.valueOf(finalI + 1);

                                // Check if the slot is occupied
                                if (slots.contains(slotNumber)) {
                                    // Slot is occupied, display a message
                                    displayOccupiedSlotMessage();
                                } else {
                                    getPeronId();
                                    requestReservation(personId, floorNumber, slotNumber);
                                    changeSlotBackground(textView, false);
                                }
                            });
                        }
                    } else {
                        // Handle null or empty response
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            private void displayOccupiedSlotMessage() {
                Toast.makeText(ReservationActivity.this, "This slot is already occupied.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PersonResponse> call, Throwable t) {
                hideLoading();
            }
        });
    }

    private void requestReservation(String personId, String floor, String slot){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("personId", personId);
        jsonObject.addProperty("floor", floor);
        jsonObject.addProperty("slot", slot);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        ApiClient apiClient = new ApiClient();
        ApiEndpoints apiService = apiClient.getApiService();
        Call<PersonResponse> call = apiService.requestReservation(requestBody);

        call.enqueue(new Callback<PersonResponse>() {
            @Override
            public void onResponse(Call<PersonResponse> call, Response<PersonResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReservationActivity.this, "Reservation request successfully.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(ReservationActivity.this, "Reservation request failed.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PersonResponse> call, Throwable t) {
                Toast.makeText(ReservationActivity.this, "Error: requesting reservation.", Toast.LENGTH_SHORT).show();
            }
        });
    };

    private void getPeronId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String getPersonId = sharedPreferences.getString("personId", "");

        // Now you can use fullName, username, and contactNumber to display the user's information
        if (!getPersonId.isEmpty() ) {
            personId = getPersonId;
        } else {
            // Handle the case where the data is not available
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        lotsContainer.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        lotsContainer.setVisibility(View.VISIBLE);
    }
}
