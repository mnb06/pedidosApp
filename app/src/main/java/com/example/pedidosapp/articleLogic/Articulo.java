package com.example.pedidosapp.articleLogic;

import java.util.ArrayList;

public class Articulo {

    private String nombre;
    private String stock;
    private String stockMin;

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    private String cantidad;


    public Articulo() {
    }

    public Articulo(String nombre, String stock){
        this.nombre = nombre;
        this.stock = stock;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStockMin() {
        return stockMin;
    }

    public void setStockMin(String stockMin) {
        this.stockMin = stockMin;
    }
}
