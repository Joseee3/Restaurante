package com.example.restaurante.Entities;

import java.util.List;
import java.util.Date;

public class Pedido {
    private String id;
//    private String fecha;
    private Double total;
    private List<Carrito> carritolist;
    private Date date;
    private String userUid;

    private List<Platos> platosList;

    // Constructor vacío necesario para Firebase
    public Pedido() {}

    public Pedido(List<Carrito> carritolist, Date date, double total) {
        this.carritolist = carritolist;
        this.date = date;
        this.total = total;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getFecha() {
//        return fecha;
//    }
//
//    public void setFecha(String fecha) {
//        this.fecha = fecha;
//    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

//    public List<Platos> getPlatos() {
//        return platos;
//    }
//
//    public void setPlatos(List<Platos> platos) {
//        this.platos = platos;
//    }


    public List<Carrito> getCarritolist() {
        return carritolist;
    }

    public void setCarritolist(List<Carrito> carritolist) {
        this.carritolist = carritolist;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id='" + id + '\'' +
                ", total=" + total +
                ", date=" + date +
                ", carrito size=" + carritolist.size() +
                '}';
    }
}