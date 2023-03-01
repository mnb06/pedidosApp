package com.example.pedidosapp.pedidosLogic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pedidosapp.R;
import com.example.pedidosapp.articleLogic.Articulo;

import java.util.ArrayList;

public class AdapterDetail extends RecyclerView.Adapter<AdapterDetail.MyViewHolder> {

    Context context;

    ArrayList<Articulo> list;

    // Constructor
    public AdapterDetail(Context context, ArrayList<Articulo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_pedido_detail, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Articulo articulo = list.get(position);
        holder.nombre.setText(articulo.getNombre());
        holder.cant.setText(articulo.getCantidad() + " KG");
        holder.setIsRecyclable(false);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, cant;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.detailArt);
            cant = itemView.findViewById(R.id.detailCant);
        }
    }
}
