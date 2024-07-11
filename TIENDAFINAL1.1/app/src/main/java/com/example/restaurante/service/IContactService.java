package com.example.restaurante.service;


import com.example.restaurante.Entities.Contact;
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

public interface IContactService {

    @GET("/contacts")
    Call<List<Contact>> getAll(@Query("name") String name, @Query("limit") int limit, @Query("page") int page);
    @GET("/contacts")
    Call<List<Contact>> getAll(@Query("name") String name);

    @GET("/contacts/{id}")
    Call<Contact> find(@Path("id") int id);

    @POST("/contacts")
    Call<Contact> create(@Body Platos contact);

    @PUT("/contacts/{id}")
    Call <Contact> update(@Path("id") int id, @Body Contact contact);

    @DELETE("/contacts/{id}")
    Call <Void> delete(@Path("id") int id);



}