package com.example.pedidosapp.pedidosLogic.articles;

import android.content.Context;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pedidosapp.R;
import com.example.pedidosapp.articleLogic.Articulo;

import java.util.ArrayList;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.MyViewHolder> {

    Context context;

    ArrayList<Articulo> list;

    // Constructor
    public ArticlesAdapter(Context context, ArrayList<Articulo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_add_article, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Articulo articulo = list.get(position);
        holder.nombre.setText(articulo.getNombre());
        holder.stock.setFilters(new InputFilter[]{ new MaxStockControl("1", articulo.getStock())});

        holder.setIsRecyclable(false);

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, articulos;
        EditText stock;
        Button add, edit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.articleName);
            stock = itemView.findViewById(R.id.articleStock);
            add = itemView.findViewById(R.id.articleAdd);


        }
    }
}


