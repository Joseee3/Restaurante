package com.example.restaurante.service;

import com.example.restaurante.Entities.Profile;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IProfileService {
    @GET("Usuario")
    Call<Profile> login(@Query("email") String email, @Query("password") String password);
    @GET("Usuario")
    Call<List<Profile>> getAllUsers();
    @GET("Usuario/{id}")
    Call<Profile> find(@Path("id") int id);

    @POST("Usuario")
    Call<Profile> create(@Body Profile profile);

    @PUT("Usuario/{id}")
    Call <Profile> update(@Path("id") int id, @Body Profile profile);

    @GET("Usuario")
    Call<Profile> findByEmail(@Query("email") String email);
}
