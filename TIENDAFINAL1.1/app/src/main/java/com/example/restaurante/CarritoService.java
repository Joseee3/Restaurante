package com.example.restaurante;

import com.example.restaurante.service.ICarritoService;

import java.util.ArrayList;
import java.util.List;

import com.example.restaurante.Entities.Carrito;

import retrofit2.Call;

public class CarritoService implements ICarritoService {
    private List<Carrito> carritoList;

    private static CarritoService instance;

    private CarritoService() {
        carritoList = new ArrayList<>();
        // Carga los elementos del carrito en carritoList desde la base de datos o donde los estés almacenando
    }

    public static CarritoService getInstance() {
        if (instance == null) {
            instance = new CarritoService();
        }
        return instance;
    }

    public List<Carrito> clearCarrito() {
        List<Carrito> items = new ArrayList<>(carritoList);
        carritoList.clear();
        // Aquí también deberías actualizar la base de datos o donde estés almacenando los elementos del carrito
        return items;
    }

    @Override
    public Call<List<Carrito>> getAll(String name, int limit, int page) {
        return null;
    }

    @Override
    public Call<List<Carrito>> getAll(String name) {
        return null;
    }

    @Override
    public Call<List<Carrito>> getAll() {
        return null;
    }

    @Override
    public Call<Carrito> find(int id) {
        return null;
    }

    @Override
    public Call<Carrito> create(Carrito carrito) {
        return null;
    }

    @Override
    public Call<Carrito> update(int id, Carrito carrito) {
        return null;
    }

    @Override
    public Call<Void> delete(int id) {
        return null;
    }

    @Override
    public Call<List<Carrito>> getCarrito() {
        return null;
    }

    @Override
    public Call<Carrito> updateCantidad(int id, Carrito carrito) {
        return null;
    }
}