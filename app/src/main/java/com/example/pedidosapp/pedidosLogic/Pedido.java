package com.example.pedidosapp.pedidosLogic;

import com.example.pedidosapp.articleLogic.Articulo;
import com.example.pedidosapp.clientsLogic.Client;

import java.util.Date;
import java.util.List;

public class Pedido {

    private String cliente;
    private List<Articulo> listArticulos;
    private Date fecha;

    public Pedido() {
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public List<Articulo> getListArticulos() {
        return listArticulos;
    }

    public void setListArticulos(List<Articulo> listArticulos) {
        this.listArticulos = listArticulos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void addArticulo(Articulo articulo){
        listArticulos.add(articulo);
    }

}
