package com.example.onlineparking;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.onlineparking.Dashboard.DashboardActivity;

public class TermsConditionActivity extends AppCompatActivity {
    AppCompatButton btnGoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);

        btnGoHome = findViewById(R.id.backHome);
        btnGoHome.setOnClickListener(view -> {
            startActivity(new Intent(TermsConditionActivity.this, DashboardActivity.class));
        });
    }
}
