package com.example.onlineparking;
import android.content.Intent;
import android.graphics.RegionIterator;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.onlineparking.Dashboard.DashboardActivity;

public class AboutSystemActivity extends AppCompatActivity {
    AppCompatButton btnGoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_system);

        btnGoHome = findViewById(R.id.backHome);
        btnGoHome.setOnClickListener(view -> {
            startActivity(new Intent(AboutSystemActivity.this, DashboardActivity.class));
        });
    }
}
