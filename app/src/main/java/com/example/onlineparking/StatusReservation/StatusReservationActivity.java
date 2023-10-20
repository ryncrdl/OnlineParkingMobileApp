package com.example.onlineparking.StatusReservation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineparking.Api.ApiClient;
import com.example.onlineparking.Api.ApiEndpoints;
import com.example.onlineparking.Dashboard.DashboardActivity;
import com.example.onlineparking.Login.PersonResponse;
import com.example.onlineparking.R;
import com.example.onlineparking.Reservation.ReservationActivity;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusReservationActivity extends AppCompatActivity {

    private List<PersonResponse> reservations;
    private StatusReservationAdapter statusAdapter;
    private ProgressBar progressBar;

    private String personId;

    AppCompatButton btnDashboard;

    RecyclerView reservationRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_reservation);

        btnDashboard = findViewById(R.id.backHome);

        btnDashboard.setOnClickListener(view -> {
            startActivity(new Intent(StatusReservationActivity.this, DashboardActivity.class));
        });

        progressBar = findViewById(R.id.progress_bar);
        reservationRecyclerView = findViewById(R.id.view_reservations);
        reservationRecyclerView.setLayoutManager(new LinearLayoutManager(StatusReservationActivity.this));
        reservations = new ArrayList<>();
        statusAdapter = new StatusReservationAdapter(StatusReservationActivity.this, reservations);
        reservationRecyclerView.setAdapter(statusAdapter);

        fetchServices();
    }

    public void fetchServices() {
        showLoading();
        getPeronId();
        ApiClient apiClient = new ApiClient();
        ApiEndpoints apiService = apiClient.getApiService();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("personId", personId);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<List<PersonResponse>> call = apiService.getReservationById(requestBody);

        call.enqueue(new Callback<List<PersonResponse>>() {
            @Override
            public void onResponse(Call<List<PersonResponse>> call, Response<List<PersonResponse>> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    List<PersonResponse> fetchedReservations = response.body();
                    if (fetchedReservations != null && !fetchedReservations.isEmpty()) {
                        reservations.addAll(fetchedReservations);
                        statusAdapter.notifyDataSetChanged();
                    } else {
                        // Handle empty or null response
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<List<PersonResponse>> call, Throwable t) {
                // hideLoading();
            }
        });
    }

    private void getPeronId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String getPersonId = sharedPreferences.getString("personId", "");
        if (!getPersonId.isEmpty() ) {
            personId = getPersonId;
        } else {
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        reservationRecyclerView.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        reservationRecyclerView.setVisibility(View.VISIBLE);
    }
}
