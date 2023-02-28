package com.example.pedidosapp.pedidosLogic;

import com.example.pedidosapp.articleLogic.ArticuloDetail;

import java.io.Serializable;
import java.util.ArrayList;

public class Pedido implements Serializable {

    private String cliente, fecha;
    private ArrayList<ArticuloDetail> listArticulos;


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

    public void addArticulo(ArticuloDetail art){
        this.listArticulos.add(art);
    }

    public ArrayList<ArticuloDetail> getListArticulos() {
        return listArticulos;
    }

    public void setListArticulos(ArrayList<ArticuloDetail> listArticulos) {
        this.listArticulos = listArticulos;
    }


}
