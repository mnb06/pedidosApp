package com.example.pedidosapp.pedidosLogic;

import com.example.pedidosapp.articleLogic.Articulo;
import java.util.Date;
import java.util.List;

public class Pedido {

    private String cliente, fecha;
    //private List<Articulo> listArticulos;

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


}
