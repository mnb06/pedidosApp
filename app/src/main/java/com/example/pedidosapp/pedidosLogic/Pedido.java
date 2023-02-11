package com.example.pedidosapp.pedidosLogic;

import com.example.pedidosapp.articleLogic.Articulo;
import com.example.pedidosapp.clientsLogic.Client;

import java.util.Date;
import java.util.List;

public class Pedido {

    private Client cliente;
    private List<Articulo> listArticulos;
    private String fecha;

    public Pedido() {
    }

    public Client getCliente() {
        return cliente;
    }

    public void setCliente(Client cliente) {
        this.cliente = cliente;
    }

    public List<Articulo> getListArticulos() {
        return listArticulos;
    }

    public void setListArticulos(List<Articulo> listArticulos) {
        this.listArticulos = listArticulos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void addArticulo(Articulo articulo){
        listArticulos.add(articulo);
    }

}
