package com.example.pedidosapp.articleLogic;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pedidosapp.R;

import java.util.ArrayList;

import com.example.pedidosapp.clientsLogic.Client;
import com.example.pedidosapp.clientsLogic.ClientEdit;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AdapterArt extends RecyclerView.Adapter<AdapterArt.MyViewHolder> {

    Context context;
    DatabaseReference databaseReference;

    ArrayList<Articulo> list;

    // Constructor
    public AdapterArt(Context context, ArrayList<Articulo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Articulo articulo = list.get(position);
        holder.nombre.setText(articulo.getNombre());
        holder.stock.setText(articulo.getStock());
        holder.stockMin.setText(articulo.getStockMin());
        holder.setIsRecyclable(false);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference("Articulos");
                ref.child(articulo.getNombre()).setValue(null);
                Toast.makeText(context.getApplicationContext(), "Eliminado satisfactoriamente.",
                        Toast.LENGTH_LONG).show();
                list.clear();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArticuloEdit.class);
                intent.putExtra("name", articulo.getNombre());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, stock, stockMin;
        Button delete, edit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.artName);
            stock = itemView.findViewById(R.id.artStock);
            stockMin = itemView.findViewById(R.id.artStockMin);
            delete = itemView.findViewById(R.id.artDelete);
            edit = itemView.findViewById(R.id.artEdit);


        }
    }
}
