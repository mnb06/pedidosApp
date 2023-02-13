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
import com.example.pedidosapp.pedidosLogic.Pedido;

import java.util.ArrayList;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.MyViewHolder> {

    Context context;

    ArrayList<Articulo> list;
    public ArrayList<Articulo> elegidos;



    // Constructor
    public ArticlesAdapter(Context context, ArrayList<Articulo> list) {
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

        // Control para que no se pueda seleccionar mayor cantidad que el stock actual
        holder.stock.setFilters(new InputFilter[]{ new MaxStockControl("1", articulo.getStock())});

        holder.setIsRecyclable(false);


        // Agrega el articulo al pedido
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Articulo elegido = new Articulo(articulo.getNombre(),holder.stock.getText().toString());

                elegidos.add(elegido);

                // Notificacion Toast para mostrar si el articulo fue cargado
                Toast.makeText(context, "Articulo cargado correctamente",
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


