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
    public ArrayList<Articulo> elegidos;

    // Constructor
    public ArticlesAdapter(Context context, ArrayList<Articulo> list){
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_add_article, parent, false);
        elegidos = new ArrayList<>();
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // Traer el articulo
        Articulo articulo = list.get(position);
        holder.nombre.setText(articulo.getNombre());
        holder.stock.setText("0");

        holder.setIsRecyclable(false);


        // Agrega el articulo al pedido
        holder.add.setOnClickListener(view -> {
            // Verifica que no se agreguen articulos con 0kg
            if(holder.stock.getText().toString().equals("0")){
                Toast.makeText(context, "Ingrese un valor mayor a 0",
                        Toast.LENGTH_SHORT).show();
            }else {
                Articulo selected = new Articulo(articulo.getNombre(), holder.stock.getText().toString());
                elegidos.add(selected);

                // Notificacion Toast para mostrar si el articulo fue cargado
                Toast.makeText(context, "Articulo agregado a la lista",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nombre;
        EditText stock;
        Button add;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.articleName);
            stock = itemView.findViewById(R.id.articleStock);
            add = itemView.findViewById(R.id.articleAdd);


        }
    }
}


