package com.example.restaurante.Entities;

import java.io.Serializable;

import com.example.restaurante.Entities.Platos;

public class Carrito {

    public int id;

    public String name;
    public String image;
    private double price;

    public int cantidad;

    private double total;

    private Platos plato;

    public Carrito() {
    }
    //
//public Platos getPlato() {
//    // Crea un nuevo objeto Platos con los valores de este objeto Carrito
//    Platos plato = new Platos(this.name, this.cantidad, this.image, this.price, this.total);
//    // Establece los valores adicionales
//    // plato.setImage(this.image); // Necesitarás un método setImage en la clase Platos
//    // plato.setPrice(this.price); // Necesitarás un método setPrice en la clase Platos
//    return plato;
//}


    public Platos getPlato() {
        return plato;
    }

    public Carrito(int id, String name, int cantidad, double precio, String image) {
        this.id = id;
        this.name = name;
        this.cantidad = cantidad;
        this.price = precio;
        this.image = image;
    }


    // Constructor, getters y setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Carrito{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", cantidad=" + cantidad +
                ", total=" + total +
                ", plato=" + plato +
                '}';
    }
}
