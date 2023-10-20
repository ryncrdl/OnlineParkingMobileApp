package com.example.onlineparking.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.RegionIterator;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.onlineparking.AboutSystemActivity;
import com.example.onlineparking.MainActivity;
import com.example.onlineparking.R;
import com.example.onlineparking.Reservation.ReservationActivity;
import com.example.onlineparking.StatusReservation.StatusReservationActivity;
import com.example.onlineparking.TermsConditionActivity;

public class DashboardActivity extends AppCompatActivity {

    Context context;
    private String personId;
    TextView currentUser;

    AppCompatButton btnCreateReservation, btnStatusReservation, btnAboutSystem, btnTermsCondition, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnCreateReservation = findViewById(R.id.createReservation);
        btnStatusReservation = findViewById(R.id.statusReservation);
        btnAboutSystem = findViewById(R.id.aboutSystem);
        btnTermsCondition = findViewById(R.id.termCondition);
        btnLogout = findViewById(R.id.dashboard_logout);
        currentUser = findViewById(R.id.dashboard_currentUser);

        btnCreateReservation.setOnClickListener(view -> {
            startActivity(new Intent(DashboardActivity.this, ReservationActivity.class));
        });
        btnStatusReservation.setOnClickListener(view -> {
            startActivity(new Intent(DashboardActivity.this, StatusReservationActivity.class));
        });
        btnAboutSystem.setOnClickListener(view -> {
            startActivity(new Intent(DashboardActivity.this, AboutSystemActivity.class));
        });
        btnTermsCondition.setOnClickListener(view -> {
            startActivity(new Intent(DashboardActivity.this, TermsConditionActivity.class));
        });

        btnLogout.setOnClickListener(view -> {
            logoutUser();
            // Redirect to the login or sign-in screen
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        getUserDataFromSharedPreferences();
    }

    private void logoutUser() {

        clearUserData();

        // Set the "isLoggedIn" status to false
        setLoggedInStatus(false);

        // Redirect to the sign-in activity or any other appropriate screen
        startActivity(new Intent(DashboardActivity.this, MainActivity.class));
        finish(); // Finish the current activity
    }

    private void clearUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Remove user-specific data
        editor.remove("personId");
        editor.remove("username");

        editor.apply();
    }

    private void setLoggedInStatus(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    private void getUserDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String getPersonId = sharedPreferences.getString("personId", "");
        String getUsername = sharedPreferences.getString("username", "");

        // Now you can use fullName, username, and contactNumber to display the user's information
        if (!getPersonId.isEmpty() && !getUsername.isEmpty() ) {
            personId = getPersonId;
            currentUser.setText("Current User:\n"+getUsername);
        }
    }
}