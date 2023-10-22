package com.example.onlineparking.Registration;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineparking.Api.ApiClient;
import com.example.onlineparking.Api.ApiEndpoints;
import com.example.onlineparking.MainActivity;
import com.example.onlineparking.R;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    Button btnRegister;
    TextView btnLogin;
    EditText txtFullName, txtUsername, txtPassword, txtConfirmPassword, txtEmail, txtContact, txtCarName, txtCarModel, txtCarColor, txtCarPlateNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Fields
        txtFullName = findViewById(R.id.fullName);
        txtUsername = findViewById(R.id.username);
        txtPassword = findViewById(R.id.password);
        txtConfirmPassword = findViewById(R.id.confirmPassword);
        txtEmail = findViewById(R.id.email);
        txtContact = findViewById(R.id.contactNumber);
        txtCarName = findViewById(R.id.txtCarName);
        txtCarModel = findViewById(R.id.txtCarModel);
        txtCarColor = findViewById(R.id.txtCarColor);
        txtCarPlateNumber = findViewById(R.id.txtCarPlateNumber);

        btnRegister = findViewById(R.id.registerButton);
        btnLogin = findViewById(R.id.loginButton);

        btnRegister.setOnClickListener(view -> {
            String fullName = txtFullName.getText().toString().trim();
            String username = txtUsername.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            String confirmPassword = txtConfirmPassword.getText().toString().trim();
            String email = txtEmail.getText().toString().trim();
            String contact = txtContact.getText().toString().trim();
            String car = txtCarName.getText().toString().trim();
            String model = txtCarModel.getText().toString().trim();
            String carColor = txtCarColor.getText().toString().trim();
            String plateNumber = txtCarPlateNumber.getText().toString().trim();

            validateDetails(fullName, username, password, confirmPassword, email, contact, car, model, carColor, plateNumber);
        });

        btnLogin.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void validateDetails(String fullName, String username, String password, String confirmPassword, String email, String contact, String car, String model, String carColor, String plateNumber) {
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || contact.isEmpty() || car.isEmpty() || model.isEmpty() || carColor.isEmpty() || plateNumber.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
        } else if (fullName.length() < 5) {
            Toast.makeText(this, "Full name must be at least 5 characters", Toast.LENGTH_SHORT).show();
        } else if (username.length() < 5) {
            Toast.makeText(this, "Username must be at least 5 characters", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
        } else if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email format.", Toast.LENGTH_SHORT).show();
        } else if (!isValidContact(contact)) {
            Toast.makeText(this, "Invalid contact format.", Toast.LENGTH_SHORT).show();
        } else {
            createPerson(fullName, username, password, email, contact, car, model, carColor, plateNumber);
        }
    }

    private void createPerson(String fullName, String username, String password, String email, String contact, String car, String model, String carColor, String plateNumber) {
        ApiClient apiClient = new ApiClient();
        ApiEndpoints apiService = apiClient.getApiService();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("fullname", fullName);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("contact", contact);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("slot", "Select Slot");
        jsonObject.addProperty("floor", "Select Floor");
        jsonObject.addProperty("car", car);
        jsonObject.addProperty("platenumber", plateNumber);
        jsonObject.addProperty("model", model);
        jsonObject.addProperty("carcolor", carColor);
        jsonObject.addProperty("date", "none");
        jsonObject.addProperty("timein", "none");
        jsonObject.addProperty("timeout", "none");

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<CreatePerson> call = apiService.createPerson(requestBody);
        call.enqueue(new Callback<CreatePerson>() {
            @Override
            public void onResponse(Call<CreatePerson> call, Response<CreatePerson> response) {
                if (response.isSuccessful()) {
                    // Registration was successful, handle success case here
                    Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                } else {
                    if(!response.errorBody().equals(null)){
                        try {
                            String errorResponse = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(errorResponse);
                            if (jsonObject.has("message")) {
                                String errorMessage = jsonObject.getString("message");
                                Toast.makeText(RegistrationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle unexpected error response
                                Toast.makeText(RegistrationActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegistrationActivity.this, "Error processing response.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CreatePerson> call, Throwable t) {
                // Network error, handle failure case here
                Toast.makeText(RegistrationActivity.this, "Network error. Please check your network connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidEmail(String email) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        return emailPattern.matcher(email).matches();
    }

    private boolean isValidContact(String contact) {
        // Validate contact number in the format 09xxxxxxxxx
        Pattern contactPattern = Pattern.compile("^09\\d{9}$");
        return contactPattern.matcher(contact).matches();
    }
}
