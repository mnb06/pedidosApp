package com.example.pedidosapp.articulos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pedidosapp.R;
import android.content.Context;

import java.util.ArrayList;

public class AdapterArt extends RecyclerView.Adapter<AdapterArt.MyViewHolder> {
    Context context;
    ArrayList<Articulo> list;

    // Constructor
    public AdapterArt(Context context, ArrayList<Articulo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Articulo articulo = list.get(position);
        holder.nombre.setText(articulo.getNombre());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreArticulo);
        }
    }
}

