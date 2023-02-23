package com.example.pedidosapp.pedidosLogic;

import com.example.pedidosapp.articleLogic.Articulo;
import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class Pedido implements Serializable {

    private String cliente, fecha;
    private ArrayList<Articulo> listArticulos;


    public Pedido() {
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void addArticulo(Articulo art){
        this.listArticulos.add(art);
    }

    public ArrayList<Articulo> getListArticulos() {
        return listArticulos;
    }

    public void setListArticulos(ArrayList<Articulo> listArticulos) {
        this.listArticulos = listArticulos;
    }


}
