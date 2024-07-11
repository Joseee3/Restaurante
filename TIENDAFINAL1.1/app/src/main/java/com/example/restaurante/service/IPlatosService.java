package com.example.restaurante.service;



import com.example.restaurante.Entities.Platos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IPlatosService {

    @GET("Platos")
    Call<List<Platos>> getAll(@Query("name") String name, @Query("limit") int limit, @Query("page") int page);
    @GET("Platos")
    Call<List<Platos>> getAll(@Query("name") String name);

    @GET("Platos/{id}")
    Call<Platos> find(@Path("id") int id);

    @POST("Platos")
    Call<Platos> create(@Body Platos platos);

    @PUT("Platos/{id}")
    Call <Platos> update(@Path("id") int id, @Body Platos contact);

    @DELETE("Platos/{id}")
    Call <Void> delete(@Path("id") int id);
    @GET ("Platos")
    Call<List<Platos>> getAll();

    @GET("Platos")
    Call<List<Platos>> search(@Query("name") String query);

    @GET("Platos")
    Call<List<Platos>> getByCategory(@Query("category") String category);



}
