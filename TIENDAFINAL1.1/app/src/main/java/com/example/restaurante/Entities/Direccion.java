package com.example.restaurante.Entities;

public class Direccion {
    String direccion, indicaciones, nombredireccion, tipovivienda;
    String id_user;

    public Direccion(){

    }

    public Direccion(String direccion, String indicaciones, String nombredireccion, String tipovivienda) {
        this.direccion = direccion;
        this.indicaciones = indicaciones;
        this.nombredireccion = nombredireccion;
        this.tipovivienda = tipovivienda;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public String getNombredireccion() {
        return nombredireccion;
    }

    public void setNombredireccion(String nombredireccion) {
        this.nombredireccion = nombredireccion;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getTipovivienda() {
        return tipovivienda;
    }

    public void setTipovivienda(String tipovivienda) {
        this.tipovivienda = tipovivienda;
    }

    @Override
    public String toString() {
        return "Direccion{" +
                "direccion='" + direccion + '\'' +
                ", id_user='" + id_user + '\'' +
                ", nombredireccion='" + nombredireccion + '\'' +
                '}';
    }
}
