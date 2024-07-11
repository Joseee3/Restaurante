package com.example.restaurante.service;


import com.example.restaurante.Entities.Carrito;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ICarritoService {

    @GET("Carrito")
    Call<List<Carrito>> getAll(@Query("name") String name, @Query("limit") int limit, @Query("page") int page);
    @GET("Carrito")
    Call<List<Carrito>> getAll(@Query("name") String name);

    @GET("Carrito")
    Call<List<Carrito>> getAll();

    @GET("Carrito/{id}")
    Call<Carrito> find(@Path("id") int id);

    @POST("Carrito")
    Call<Carrito> create(@Body Carrito carrito);

    @PUT("Carrito/{id}")
    Call<Carrito> update(@Path("id") int id, @Body Carrito carrito);

    @DELETE("Carrito/{id}")
    Call <Void> delete(@Path("id") int id);

    @GET("Carrito")
    Call<List<Carrito>> getCarrito();


    @PUT("Carrito/{id}")
    Call<Carrito> updateCantidad(@Path("id") int id, @Body Carrito carrito);

}
