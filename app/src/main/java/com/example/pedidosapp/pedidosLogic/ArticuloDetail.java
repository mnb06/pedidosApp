package com.example.pedidosapp.pedidosLogic;

public class ArticuloDetail {

    private String nombre;
    private String cantidad;



    public ArticuloDetail() {
    }

    public ArticuloDetail(String nombre, String cantidad){
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }





}
