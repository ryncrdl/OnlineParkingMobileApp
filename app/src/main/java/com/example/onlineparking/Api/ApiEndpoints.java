package com.example.onlineparking.Api;

import com.example.onlineparking.Login.PersonResponse;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface ApiEndpoints {

    // login
    @POST("app/application-0-ltegn/endpoint/login")
    Call<PersonResponse> loginPerson(@Body RequestBody requestBody);

    //Reservation
    @POST("app/application-0-ltegn/endpoint/checkReservation")
    Call<PersonResponse> checkSlots(@Body RequestBody requestBody);

    @POST("app/application-0-ltegn/endpoint/requestReservation")
    Call<PersonResponse> requestReservation(@Body RequestBody requestBody);

    @POST("app/application-0-ltegn/endpoint/getReservationById")
    Call<List<PersonResponse>> getReservationById(@Body RequestBody requestBody);
}
