package com.example.onlineparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineparking.Api.ApiEndpoints;
import com.example.onlineparking.Dashboard.DashboardActivity;
import com.example.onlineparking.Login.PersonResponse;
import com.example.onlineparking.Registration.RegistrationActivity;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView btnRegistration;
    Button btnLogin;
    CheckBox rememberPassword;
    EditText txtUsername, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isUserLoggedIn()) {
            // Redirect to the dashboard
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }

        btnRegistration = findViewById(R.id.registerButton);
        btnLogin =  findViewById(R.id.loginButton);
        rememberPassword = findViewById(R.id.checkRememberPassword);
        txtUsername =  findViewById(R.id.username);
        txtPassword =  findViewById(R.id.password);

        btnRegistration.setOnClickListener(view -> {
            startActivity(new Intent(this, RegistrationActivity.class));
        });



        btnLogin.setOnClickListener(view -> {
            //Check valid username and password
            String getUsername = txtUsername.getText().toString().trim();
            String getPassword = txtPassword.getText().toString().trim();
            checkUsernameAndPassword(getUsername, getPassword);
        });
    }

    private void checkUsernameAndPassword(String username, String password){
        if(username.isEmpty()){
            Toast.makeText(MainActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(MainActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }else if(!username.isEmpty() && !password.isEmpty()){
            validateCredentials(username, password);
        }
    }

    private void validateCredentials(String username, String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-east-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<PersonResponse> call = apiEndpoints.loginPerson(requestBody);
        call.enqueue(new Callback<PersonResponse>() {
            @Override
            public void onResponse(Call<PersonResponse> call, Response<PersonResponse> response) {
                PersonResponse createReponse = response.body();
                if (response.code() == 200) {
                    String personId = createReponse.get_id();
                    String username = createReponse.getUsername();


                    Toast.makeText(MainActivity.this, "Successfully login", Toast.LENGTH_SHORT).show();
                    if(rememberPassword.isChecked()){
                        setLoggedInStatus(true);
                    }
                    storeUserData(personId, username);
                    // Finish the current SignIn activity
                    finish();
                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                } else {
                    setLoggedInStatus(false);
                    Toast.makeText(MainActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<PersonResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Server failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeUserData(String personId, String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("personId", personId);
        editor.putString("username", username);

        editor.apply();
    }

    private void setLoggedInStatus(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}