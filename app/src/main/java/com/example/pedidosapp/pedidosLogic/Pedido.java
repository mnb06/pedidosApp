package com.example.pedidosapp.pedidosLogic;

import com.example.pedidosapp.articleLogic.Articulo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public ArrayList<Articulo> getListArticulos() {
        return listArticulos;
    }

    public void setListArticulos(ArrayList<Articulo> listArticulos) {
        this.listArticulos = listArticulos;
    }


}
